using System;
using System.Linq;
using System.Runtime.InteropServices;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace ImgProcess
{
    class Bgra32Image : ICloneable
    {

        public Bgra32Image Sample(uint rate)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.sample(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, rate);
            this.imgPixels = buffer;
            return this;
        }

        public Bgra32Image Quantize(uint lv)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.quantize(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, lv);
            this.imgPixels = buffer;
            return this;
        }

        public Bgra32Image HistogramEqualization()
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.histogram_equalization(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height);
            this.imgPixels = buffer;
            return this;
        }

        public Bgra32Image NeighborTransform(double deltaX, double deltaY, double angle, double scale)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.neighbor_transform(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, (float)deltaX, (float)deltaY, (float)angle, (float)scale);
            this.imgPixels = buffer;
            return this;
        }

        public Bgra32Image BilinearTransform(double deltaX, double deltaY, double angle, double scale)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.bilinear_transform(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, (float)deltaX, (float)deltaY, (float)angle, (float)scale);
            this.imgPixels = buffer;
            return this;
        }

        public Bgra32Image Filter(double[,] mat)
        {
            var ary_mat = new float[mat.Length];
            var hgt = mat.GetLength(0);
            var wid = mat.GetLength(1);

            /* 二维数组的转化：(http://bbs.csdn.net/topics/390650297) */
            foreach(var elem in Enumerable.Range(0, mat.Length).Select(i => new { i = i, x = i % wid, y = i / wid }))
                ary_mat[elem.i] = (float)mat[elem.y, elem.x];

            Filter(ary_mat, (uint)wid, (uint)hgt);
            return this;
        }

        public Bgra32Image Filter(float[] mat, uint wid, uint hgt)
        {
            /* 针对 int 版本的优化（不过可能没什么明显价值？） */
            bool allInteger = true;
            foreach(var num in mat) {
                /* [How to determine if a decimal/double is an integer?]
                 * (http://stackoverflow.com/questions/2751593/how-to-determine-if-a-decimal-double-is-an-integer)
                 */
                if(num % 1 != 0) {
                    allInteger = false;
                    break;
                }
            }

            var buffer = new byte[imgPixels.Length];

            if(allInteger) {
                var int_matrix = new int[mat.Length];
                for(int i = 0; i < mat.Length; i++)
                    int_matrix[i] = (int)mat[i];
                ImgFunc.filter(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, int_matrix, wid, hgt);
            } else
                ImgFunc.filter(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, mat, wid, hgt);

            this.imgPixels = buffer;
            return this;
        }

        public Bgra32Image Filter(string opName)
        {
            var buffer = new byte[imgPixels.Length];

            switch(opName) {
            case "roberts": ImgFunc.filter_roberts(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height); break;
            case "sobel"  : ImgFunc.filter_sobel(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height); break;
            case "median" : ImgFunc.filter_median(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height); break;
            default : throw new Exception("No such Filter!");
            }

            this.imgPixels = buffer;
            return this;
        }

        public BitmapSource ToBitmapSource()
        {
            /*
             * 参考资料：
             * [How can I convert byte[] to BitmapImage?]
             * (http://stackoverflow.com/questions/15270844/how-can-i-convert-byte-to-bitmapimage/15272528#15272528) 
             */
            return BitmapSource.Create(
                imgInfo.Width,
                imgInfo.Height,
                imgInfo.DpiX,
                imgInfo.DpiY,
                imgInfo.Format,
                null,
                imgPixels,
                imgInfo.Stride
                );
        }

        public Gray8Image ToGray8Image()
        {
            var bitmap = new FormatConvertedBitmap(ToBitmapSource(), PixelFormats.Gray8, null, 0);
            return new Gray8Image(bitmap);
        }

        public Bgra32Image(BitmapSource img)
        {
            if (img == null)
                throw new System.ArgumentNullException();

            if (img.Format != PixelFormats.Bgra32)
                img = new FormatConvertedBitmap(img, PixelFormats.Bgra32, null, 0);

            imgInfo.Width = img.PixelWidth;
            imgInfo.Height = img.PixelHeight;
            imgInfo.Format = img.Format;
            imgInfo.DpiX = img.DpiX;
            imgInfo.DpiY = img.DpiY;

            /* Stride 的计算方式 */
            imgInfo.Stride = imgInfo.Width * imgInfo.Format.BitsPerPixel / 8;

            /* 保存图片像素信息到 ImgPixels，只包含各个像素的信息 */
            imgPixels = new byte[imgInfo.Height * imgInfo.Stride];
            img.CopyPixels(imgPixels, imgInfo.Stride, 0);
        }

        public Bgra32Image(Bgra32Image rhs)
        {
            if (rhs == this)
                return;

            this.imgInfo = rhs.imgInfo;
            this.imgPixels = rhs.imgPixels.Clone() as byte[];
        }

        public object Clone()
        {
            return new Bgra32Image(this);
        }

 
        public byte[] ImagePixels { get { return imgPixels; } }
        public ImageInfo Info { get { return imgInfo; } }

        [StructLayout(LayoutKind.Sequential)]
        public struct ImageInfo
        {
            public double DpiX;
            public double DpiY;
            public int Width;
            public int Height;
            public int Stride;
            public PixelFormat Format;
        }

        private byte[] imgPixels;                           /* 数组保存的像素信息 */
        private ImageInfo imgInfo;                          /* 与 ImgPixels 对应的，图片信息 */
    }
}
