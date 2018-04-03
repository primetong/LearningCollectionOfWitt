using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace ImgProcess
{
    public static class Common
    {

        public static float String2Float(string src, float default_value = 0.0f, float min = float.MinValue, float max = float.MaxValue)
        {
            float ret;
            if (!float.TryParse(src, out ret))
                ret = default_value;

            if (ret < min)
                return min;
            else if (ret > max)
                return max;
            else 
                return ret;
        }


        public static string GetTempFilePathWithExtension(string extension)
        {
            /*
             * 来源：
             * [How can I create a temp file with a specific extension with .net?]
             * (http://stackoverflow.com/questions/581570/how-can-i-create-a-temp-file-with-a-specific-extension-with-net)
             */

            var path = Path.GetTempPath();
            var fileName = Guid.NewGuid().ToString() + extension;
            return Path.Combine(path, fileName);
        }

        public static string CreateTmpTxtFile(string content)
        {
            /*
             * 在 C# 中创建临时文件
             * [How to use Temporary Files in C#]
             * (http://www.daveoncsharp.com/2009/09/how-to-use-temporary-files-in-csharp/)
             */

            var fileName = GetTempFilePathWithExtension(".txt");
            System.IO.File.WriteAllText(fileName, content);
            return fileName;
        }


        public static void SaveTxtFileWithDialog(string content)
        {

            var dlg = new Microsoft.Win32.SaveFileDialog()
            {
                DefaultExt = "*.txt|*.*,",
                Filter = "Text File (*.txt)|*.txt"
                       + "|Any File (*.*)|*.*"
            };
            var result = dlg.ShowDialog();

            /*
             * 保存 string 为文本
             * [How to: Write to a Text File (C# Programming Guide)](https://msdn.microsoft.com/en-us/library/8bh11f1k.aspx)
             */
            if (result == true) {
                string message = null;
                try {
                    System.IO.File.WriteAllText(dlg.FileName, content);
                } catch (System.IO.PathTooLongException) {
                    message = "文件路径过长！";
                } catch (System.IO.DirectoryNotFoundException) {
                    message = "没有找到这个目录！";
                } catch (System.UnauthorizedAccessException) {
                    message = "无法执行写入操作！";
                } catch (System.Security.SecurityException) {
                    message = "没有权限执行这个操作！";
                }

                System.Windows.MessageBox.Show(
                        message,
                        "无法保存文件",
                        System.Windows.MessageBoxButton.OK,
                        System.Windows.MessageBoxImage.Warning
                        );
            }
        }

        public static BitmapImage OpenImgFile(string filePath = null)
        {

            /* 
             * 打开图片相关，参考资料：
             * [WPF image control source](http://stackoverflow.com/questions/1241156/wpf-image-control-source)
             * [Dynamic loading of images in WPF](http://stackoverflow.com/questions/569561/dynamic-loading-of-images-in-wpf)
             * [MSDN Simple Calculator](https://code.msdn.microsoft.com/Simple-Calculator-d1d8cf4c)
             */
            if(filePath == null) {
                var dlg = new Microsoft.Win32.OpenFileDialog()
                {
                    DefaultExt = "*.bmp| *.png| *.jpg| *.jpeg| *.gif| *.tif",
                    Filter = "Image files(*.bmp; *.png; *.jpg; *.jpeg; *.gif; *.tif)| *. bmp; *.png; *.jpg; *.jpeg; *.gif; *.tif"
                };

                if((bool)dlg.ShowDialog()) {
                    filePath = dlg.FileName;
                }
            }

            BitmapImage img = null;

            if(filePath != null) {
                img = new BitmapImage();
                
                /* 听说这样写可以转换图片为 bgr32
                 * [BitmapImage from file PixelFormat is always bgr32]
                 * (http://stackoverflow.com/questions/15222596/bitmapimage-from-file-pixelformat-is-always-bgr32) 
                 */

                /* [BitmapImage Constructor (Uri)](http://msdn.microsoft.com/en-us/library/ms602473.aspx) */
                img.BeginInit();
                img.UriSource = new Uri(filePath, UriKind.Absolute);        /* 加载路径中的文件 */
                img.CacheOption = BitmapCacheOption.OnLoad;                     /* 强制立即读取 */
                img.CreateOptions                                               /* 设定创建的附加选项 */
                    = BitmapCreateOptions.IgnoreImageCache
                    | BitmapCreateOptions.PreservePixelFormat
                    ;

                try {   /* 尝试初始化图像 */
                    img.EndInit();
                    /* 解除文件占用 */
                    img.Freeze();
                } catch (System.InvalidOperationException) {
                    /* 
                     * 消息窗口，参考资料：
                     * [MessageBox in WPF](http://www.c-sharpcorner.com/UploadFile/mahesh/messagebox-in-wpf/)
                     */

                    System.Windows.MessageBox.Show(
                        "未能识别这个文件类型",
                        "不支持的文件",
                        System.Windows.MessageBoxButton.OK,
                        System.Windows.MessageBoxImage.Error
                        );

                    img = null;
                }
            }

            return img;
        }

        public static void GenerateImage(BitmapSource bitmap, string lowerExtension, Stream destStream)
        {
            /* 
             * 参考资料：
             * [在WPF程序中将控件所呈现的内容保存成图像]
             * (http://www.cnblogs.com/TianFang/archive/2012/10/07/2714140.html) 
             * [Rendering a WPF Container to Bitmap]
             * (http://weblog.west-wind.com/posts/2007/Sep/10/Rendering-a-WPF-Container-to-Bitmap)
             */
            BitmapEncoder encoder = null;
            switch (lowerExtension)
            {
                case ".jpg":    { encoder = new JpegBitmapEncoder(); break; }
                case ".png":    { encoder = new PngBitmapEncoder();  break; }
                case ".bmp":    { encoder = new BmpBitmapEncoder();  break; }
                case ".gif":    { encoder = new GifBitmapEncoder();  break; }
                case ".tif":    { encoder = new TiffBitmapEncoder(); break; }
                default:        { encoder = new BmpBitmapEncoder();  break; }
            }
            encoder.Frames.Add(BitmapFrame.Create(bitmap));
            encoder.Save(destStream);
        }

        public static void SaveImgFile(BitmapSource img)
        {
            /*
             * 关于保存文件的方法：
             * [How do I show a Save As dialog in WPF?]
             * (http://stackoverflow.com/questions/5622854/how-do-i-show-a-save-as-dialog-in-wpf)
             */
             
            var dlg = new Microsoft.Win32.SaveFileDialog()
            {
                DefaultExt = "*.bmp|*.png|*.jpg|*.gif|*tif",
                Filter = "Bitmap (*.bmp)|*.bmp"
                       + "|PNG image (*.png)|*.png"
                       + "|JPEG image (*.jpg)|*.jpg"
                       + "|GIF image (*.gif)|*.gif"
                       + "|TIF image (*.tif)|*.tif"
            };

            var result = dlg.ShowDialog();
            if (result == true)
            {
                var filename = dlg.FileName;
                var Extension = System.IO.Path.GetExtension(filename).ToLower();
                var fs = File.Open(filename, FileMode.Create);

                Common.GenerateImage(img, Extension, fs);
                fs.Close();
            }
        }

        public static void SaveImgFileWithHuffmanCoding(BitmapSource img)
        {
            /*
             * 关于转换方法：
             * [Data type conversion: BitmapImage to binary or byte[]]
             * (https://social.msdn.microsoft.com/Forums/vstudio/en-US/a47b89c0-b1a2-4212-96bb-63e4d9cbe319/data-type-conversion-bitmapimage-to-binary-or-byte?forum=netfxbcl)
             */

            byte[] buf;
            int actual_size;
            {   /* 开始转换 */
                byte[] src;
                BmpBitmapEncoder encoder = new BmpBitmapEncoder();
                encoder.Frames.Add(BitmapFrame.Create(img));
                using(MemoryStream ms = new MemoryStream()) {
                    encoder.Save(ms);
                    src = ms.ToArray();
                }

                /* 转换结果 */
                buf = new byte[src.Length + 4096];
                actual_size = ImgFunc.huffman_encode(src, (uint)src.Length, buf, (uint)buf.Length);

                /* 检查转换是否出现错误 */
                if(actual_size < 0) {
                    string message;
                    switch(actual_size) {
                    case -3:
                    case -1: { message = "内部错误，缓冲区分配不足"; break; }
                    case -2: { message = "内部错误，创建哈夫曼树失败"; break; }
                    default: { message = "未知的错误"; break; }
                    }

                    System.Windows.MessageBox.Show(
                        message,
                        "保存文件失败",
                        System.Windows.MessageBoxButton.OK,
                        System.Windows.MessageBoxImage.Error
                        );
                    return;
                }
            }

            var dlg = new Microsoft.Win32.SaveFileDialog()
            {
                DefaultExt = "*.hfm",
                Filter = "Binary File with Huffman Coding (*.hfm)|*.hfm"
            };

            var result = dlg.ShowDialog();
            if(result == true) {
                var fs = File.Open(dlg.FileName, FileMode.Create);
                fs.Write(buf, 0, actual_size);
                fs.Close();
            }
        }

        public static BitmapImage OpenImgFileWithHuffmanCoding(string filePath = null)
        {
            if(filePath == null) {
                var dlg = new Microsoft.Win32.OpenFileDialog()
                {
                    DefaultExt = "*.hfm",
                    Filter = "Binary File with Huffman Coding (*.hfm)|*.hfm"
                };

                if((bool)dlg.ShowDialog())
                    filePath = dlg.FileName;
            }


            if(filePath != null) {
                /*
                 * 参考资料：
                 * [BitmapImage sourceStream is null in WPF]
                 * (http://stackoverflow.com/questions/17838006/bitmapimage-sourcestream-is-null-in-wpf)
                 */
                byte[] src = File.ReadAllBytes(filePath);

                /* 转换结果 */
                var buf = new byte[src.Length * 8];
                var actual_size = ImgFunc.huffman_decode(src, (uint)src.Length, buf, (uint)buf.Length);

                /* 检查转换是否出现错误 */
                if(actual_size < 0) {
                    string message;
                    switch(actual_size) {
                    case -1: { message = "这不是一个有效的哈夫曼压缩文件"; break; }
                    case -2: { message = "无法建立哈夫曼树进行解码"; break; }
                    case -3: { message = "解码所分配的缓冲区不足，无法继续解码"; break; }
                    case -4: { message = "文件数据不完整，无法继续解码"; break; }
                    default: { message = "未知的错误"; break; }
                    }

                    System.Windows.MessageBox.Show(
                        message,
                        "无法打开文件",
                        System.Windows.MessageBoxButton.OK,
                        System.Windows.MessageBoxImage.Error
                        );
                    return null;
                }

                var img = new BitmapImage();
                try {   /* 尝试初始化图像 */
                    img.BeginInit();
                    img.StreamSource = new MemoryStream(buf, 0, actual_size);
                    img.EndInit();
                } catch(System.InvalidOperationException) {
                    System.Windows.MessageBox.Show(
                        "未能识别这个文件类型",
                        "不支持的文件",
                        System.Windows.MessageBoxButton.OK,
                        System.Windows.MessageBoxImage.Error
                        );
                    img = null;
                }
                
                return img;
            }

            return null;
        }
    }
    
}
