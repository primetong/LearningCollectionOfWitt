using System;
using System.Data;
using System.IO;
using System.Runtime.InteropServices;
using System.Text;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace ImgProcess
{
    /// <summary>
    /// 导入的动态链接库
    /// </summary>
    public static class ImgFunc
    {
        /*
         * 笔记：
         *
         * 使用的时候遇到点小问题，
         * 1. 没法直接将 ImgFuncLib 项目添加到 ImgProcess 项目，（但是反之可以，不过这样并没有用）
         * 2. 没有默认编译 ImgFuncDll，每次修改 ImgFuncDll 项目，需要手动生成一次，很烦躁…
         * 最后临时解决方案是：
         * 1. 将 ImgProcess 项目的生成路径修改得和 ImgFuncDll 一样，这样 dll 文件就可以和 exe 在同一目录了
         * 2. 选中解决方案，右键，属性，配置属性，配置，项目里，勾选上 ImgFuncDll 的“生成”选项，这样就可以一起编译了
         */

        /* 导入 Dll 参考 ImgFuncLib.hpp 的注释，或者 [在WPF中使用C++编写的DLL](http://blog.sina.com.cn/s/blog_7cc533310101mda6.html) */

        [DllImport("ImgFuncLib.dll", EntryPoint = "is_loaded", CallingConvention = CallingConvention.Cdecl)]
        public static extern bool is_loaded();

        [DllImport("ImgFuncLib.dll", EntryPoint = "sample", CallingConvention = CallingConvention.Cdecl)]
        public static extern void sample(byte[] bgra32src, byte[] dest, uint wid, uint hgt, uint rate);

        [DllImport("ImgFuncLib.dll", EntryPoint = "quantize", CallingConvention = CallingConvention.Cdecl)]
        public static extern void quantize(byte[] bgra32src, byte[] dest, uint wid, uint hgt, uint lv);

        [DllImport("ImgFuncLib.dll", EntryPoint = "mask", CallingConvention = CallingConvention.Cdecl)]
        public static extern void mask(byte[] gray8src, byte[] dest, uint wid, uint hgt, uint mask);

        [DllImport("ImgFuncLib.dll", EntryPoint = "threshold", CallingConvention = CallingConvention.Cdecl)]
        public static extern void threshold(byte[] gray8src, byte[] dest, uint wid, uint hgt, uint min, uint max);

        [DllImport("ImgFuncLib.dll", EntryPoint = "histogram", CallingConvention = CallingConvention.Cdecl)]
        public static extern void histogram(byte[] gray8src, uint wid, uint hgt, uint[] cnt, uint[] data);

        [DllImport("ImgFuncLib.dll", EntryPoint = "img2txt", CallingConvention = CallingConvention.Cdecl)]
        public static extern void img2txt(byte[] gray8src, uint wid, uint hgt, byte[] buffer, ref uint length);

        [DllImport("ImgFuncLib.dll", EntryPoint = "gamma_correction", CallingConvention = CallingConvention.Cdecl)]
        public static extern void gamma_correction(byte[] gray8src, byte[] dst, uint wid, uint hgt, float r, float eps);

        [DllImport("ImgFuncLib.dll", EntryPoint = "logarithmic_transformation", CallingConvention = CallingConvention.Cdecl)]
        public static extern void logarithmic_transformation(byte[] gray8src, byte[] dst, uint wid, uint hgt, float c, float v);

        [DllImport("ImgFuncLib.dll", EntryPoint = "contrast_stretching", CallingConvention = CallingConvention.Cdecl)]
        public static extern void contrast_stretching(byte[] gray8src, byte[] dst, uint wid, uint hgt, float a, float b);

        [DllImport("ImgFuncLib.dll", EntryPoint = "histogram_equalization", CallingConvention = CallingConvention.Cdecl)]
        public static extern void histogram_equalization(byte[] bgra32src, byte[] dst, uint wid, uint hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "neighbor_transform", CallingConvention = CallingConvention.Cdecl)]
        public static extern void neighbor_transform(byte[] bgra32src, byte[] dst, uint wid, uint hgt, float deltaX, float deltaY, float angle, float scale);

        [DllImport("ImgFuncLib.dll", EntryPoint = "bilinear_transform", CallingConvention = CallingConvention.Cdecl)]
        public static extern void bilinear_transform(byte[] bgra32src, byte[] dst, uint wid, uint hgt, float deltaX, float deltaY, float angle, float scale);

        [DllImport("ImgFuncLib.dll", EntryPoint = "filter", CallingConvention = CallingConvention.Cdecl)]
        public static extern void filter(byte[] bgra32src, byte[] dst, uint wid, uint hgt, float[] mat, uint mat_wid, uint mat_hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "filter_i", CallingConvention = CallingConvention.Cdecl)]
        public static extern void filter(byte[] bgra32src, byte[] dst, uint wid, uint hgt, int[] mat, uint mat_wid, uint mat_hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "filter_roberts", CallingConvention = CallingConvention.Cdecl)]
        public static extern void filter_roberts(byte[] bgra32src, byte[] dst, uint wid, uint hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "filter_sobel", CallingConvention = CallingConvention.Cdecl)]
        public static extern void filter_sobel(byte[] bgra32src, byte[] dst, uint wid, uint hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "filter_median", CallingConvention = CallingConvention.Cdecl)]
        public static extern void filter_median(byte[] bgra32src, byte[] dst, uint wid, uint hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "morphology_expand", CallingConvention = CallingConvention.Cdecl)]
        public static extern void morphology_expand(byte[] gray8src, byte[] dst, uint wid, uint hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "morphology_corrode", CallingConvention = CallingConvention.Cdecl)]
        public static extern void morphology_corrode(byte[] gray8src, byte[] dst, uint wid, uint hgt);

        [DllImport("ImgFuncLib.dll", EntryPoint = "huffman_encode", CallingConvention = CallingConvention.Cdecl)]
        public static extern int huffman_encode(byte[] src, uint src_size, byte[] dst, uint dst_size);

        [DllImport("ImgFuncLib.dll", EntryPoint = "huffman_decode", CallingConvention = CallingConvention.Cdecl)]
        public static extern int huffman_decode(byte[] src, uint src_size, byte[] dst, uint dst_size);


        public static ImageSource GenerateHistogram(uint[] cnt)
        {
            const uint WIDTH = 256u;
            const uint HEIGHT = 256u;
            var buffer = new byte[WIDTH * HEIGHT * 4u];

            /* 找出数量最大值 */
            uint max_cnt = 0u;
            for (uint i = 0u; i < WIDTH; i++)
                if (max_cnt < cnt[i])
                    max_cnt = cnt[i];

            /* 为 buffer 赋值 */
            for (uint w = 0u; w < WIDTH; w++)
            {
                uint h_begin = HEIGHT - (cnt[w] * (HEIGHT - 1u) / max_cnt);
                uint h = h_begin;

                while (h < HEIGHT)
                    buffer[((h++) * WIDTH + w) << 2 | 3] = 255;
            }

            /* 返回一个 256 * 256 的直方图 */
            return BitmapSource.Create(
                (int)WIDTH,
                (int)HEIGHT,
                96,
                96,
                PixelFormats.Bgra32,
                null,
                buffer,
                (int)WIDTH * PixelFormats.Bgra32.BitsPerPixel / 8
                );
        }

        public static string Bitmap2string(BitmapSource img)
        {
            /* 转换图片为灰度图并拆分为数组 */

            var gray8_img = new Gray8Image(img);

            var pixels = gray8_img.ImagePixels;
            var info = gray8_img.Info;

            /* 转换图片像素数组转化为字符画 */
            /* 如果 buffer 长度不够，将不会执行转换 */

            var length = (uint)((info.Width + 2) * (info.Height >> 1));
            var buffer = new byte[length];
            ImgFunc.img2txt(pixels, (uint)info.Width, (uint)info.Height, buffer, ref length);

            /*
             * [How to convert byte[] to string?](http://stackoverflow.com/questions/1003275/how-to-convert-byte-to-string)
             */

            var ret = System.Text.Encoding.UTF8.GetString(buffer, 0, (int)length);

            return ret;
        }


        public static DataTable ConvertArray2DToDataTable<T>(T[,] matrix)
        {
            /* [Convert and use DataTable in WPF DataGrid?]
             * (http://stackoverflow.com/questions/6984686/convert-and-use-datatable-in-wpf-datagrid) 
             * [Binding a WPF DataGrid to a DataTable]
             * (http://stackoverflow.com/questions/17915840/binding-a-wpf-datagrid-to-a-datatable)
             * [DataColumn Class]
             * (https://msdn.microsoft.com/en-us/library/system.data.datacolumn(v=vs.110).aspx)
             * [How to hide the Column header in a WPF DataGrid?]
             * (http://stackoverflow.com/questions/1075902/how-to-hide-the-column-header-in-a-wpf-datagrid)
             */

            var hgt = matrix.GetLength(0);
            var wid = matrix.GetLength(1);
            var table = new DataTable(string.Empty);

            /* 生成列 */
            for(var x = 0; x < wid; x++)
                table.Columns.Add(string.Empty, typeof(double));

            /* 生成每一行 */
            for(var y = 0; y < hgt; y++) {
                var row = table.NewRow();
                for(int x = 0; x < wid; x++)
                    row[x] = matrix[y, x];
                table.Rows.Add(row);
            }

            return table;
        }

        public static T[,] ConvertDataTableToArray2D<T>(DataTable table)
        {
            var matrix = new T[table.Rows.Count, table.Columns.Count];
            var hgt = matrix.GetLength(0);
            var wid = matrix.GetLength(1);

            for(var y = 0; y < hgt; y++)
                for(int x = 0; x < wid; x++)
                    matrix[y, x] = (T)table.Rows[y][x];

            return matrix;
        }


        public static T[,] ResizeArray2D<T>(T[,] original, int wid, int hgt)
        {
            /* 
             * [How to resize multidimensional (2D) array in C#?]
             * (http://stackoverflow.com/questions/6539571/how-to-resize-multidimensional-2d-array-in-c)
             */

            int old_hgt = original == null ? 0: original.GetLength(0);
            int old_wid = original == null ? 0: original.GetLength(1);
            var ret = new T[hgt, wid];
            int min_hgt = Math.Max(Math.Min(hgt, old_hgt), 1);
            int min_wid = Math.Max(Math.Min(wid, old_wid), 1);

            for(int y = 0; y < min_hgt; y++)
                for(int x = 0; x < min_wid; x++)
                    ret[y, x] = original[y, x];

            return ret;
        }
    }


}
