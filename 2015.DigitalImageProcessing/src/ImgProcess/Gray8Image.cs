using System;
using System.Runtime.InteropServices;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace ImgProcess
{
    class Gray8Image : ICloneable
    {

        public Gray8Image GrayProcessing(uint bit)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.mask(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, bit);
            this.imgPixels = buffer;
            return this;
        }

        public Gray8Image Threshold(uint min, uint max)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.threshold(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, min, max);
            this.imgPixels = buffer;
            return this;
        }

        public Gray8Image GammaCorrection(float r, float eps)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.gamma_correction(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, r, eps);
            this.imgPixels = buffer;
            return this;
        }

        public Gray8Image LogarithmicTransformation(float c, float v)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.logarithmic_transformation(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, c, v);
            this.imgPixels = buffer;
            return this;
        }

        public Gray8Image ContrastStretching(float a, float b)
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.contrast_stretching(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height, a, b);
            this.imgPixels = buffer;
            return this;
        }

        public Gray8Image MorphologyExpand()
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.morphology_expand(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height);
            this.imgPixels = buffer;
            return this;
        }

        public Gray8Image MorphologyCorrode()
        {
            byte[] buffer = new byte[imgPixels.Length];
            ImgFunc.morphology_corrode(imgPixels, buffer, (uint)imgInfo.Width, (uint)imgInfo.Height);
            this.imgPixels = buffer;
            return this;
        }

        public Gray8Image Histogram(out ImageSource histogram, out int mean, out int mid, out int sd, out int sum)
        {
            var cnt = new uint[256u];
            var data = new uint[4u];
            ImgFunc.histogram(imgPixels, (uint)imgInfo.Width, (uint)imgInfo.Height, cnt, data);

            histogram = ImgFunc.GenerateHistogram(cnt);

            /* data[] 分别：{灰度均值，灰度中值，灰度方差，像素总数} */
            mean    = (int)data[0];
            mid     = (int)data[1];
            sd      = (int)data[2];
            sum     = (int)data[3];

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

        public Bgra32Image ToBgra32Image()
        {
            var bitmap = new FormatConvertedBitmap(ToBitmapSource(), PixelFormats.Bgra32, null, 0);
            return new Bgra32Image(bitmap);
        }

        public Gray8Image(BitmapSource img)
        {
            if (img == null)
                throw new System.ArgumentNullException();

            if (img.Format != PixelFormats.Gray8)
                img = new FormatConvertedBitmap(img, PixelFormats.Gray8, null, 0);

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

        public Gray8Image(Gray8Image rhs)
        {
            if (rhs == this)
                return;

            this.imgInfo = rhs.imgInfo;
            this.imgPixels = rhs.imgPixels.Clone() as byte[];
        }

        public object Clone()
        {
            return new Gray8Image(this);
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
