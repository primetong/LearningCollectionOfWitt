using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.IO;
using System.Text.RegularExpressions;
using System.Reflection;
using System.Data;

namespace ImgProcess
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void buttonAbout_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show(
                "用于展示数图作业的小程序。\n仓库地址：https://github.com/wiryls/HomeworkCollectionOfMYLS",
                "关于本程序",
                MessageBoxButton.OK,
                MessageBoxImage.Asterisk
                );
        }

        private void buttonOpen_Click(object sender, RoutedEventArgs e)
        {
            var img = Common.OpenImgFile();

            if (img != null)
                /* 记录图像数据 */
                LoadImg(this.imgSrc = img);
        }

        private void MainWindow_Drop(object sender, DragEventArgs e)
        {
            /* [Drag and drop files into WPF]
             * (http://stackoverflow.com/questions/5662509/drag-and-drop-files-into-wpf)
             */
            if(e.Data.GetDataPresent(DataFormats.FileDrop)) {
                string[] files = (string[])e.Data.GetData(DataFormats.FileDrop);

                var img = Common.OpenImgFile(files[0]);

                if(img != null)
                    /* 记录图像数据 */
                    LoadImg(this.imgSrc = img);
            }
        }

        private void ButtonPointOps_Click(object sender, RoutedEventArgs e)
        {
            if (!(e.Source is Button) || this.imgSrc == null)
                return;

            var btn = e.Source as Button;

            if(btn == buttonPointOps_Gamma) {
                var r = Common.String2Float(this.textBoxPointOps_Gamma_r.Text, 1.0f, 0.0f);
                var eps = Common.String2Float(this.textBoxPointOps_Gamma_eps.Text, 1e-30f, 1e-30f, 0.1f);
                this.textBoxPointOps_Gamma_r.Text = r.ToString();
                this.textBoxPointOps_Gamma_eps.Text = eps.ToString();

                this.imageView.Source 
                    = this.imgRes 
                    = new Gray8Image(this.gray8ImgSrc).GammaCorrection(r, eps).ToBitmapSource();

            } else if(btn == buttonPointOps_Log) {
                var c = Common.String2Float(this.textBoxPointOps_Log_c.Text);
                var v = Common.String2Float(this.textBoxPointOps_Log_v.Text, 1, 1e-30f);
                this.textBoxPointOps_Log_c.Text = c.ToString();
                this.textBoxPointOps_Log_v.Text = v.ToString();

                this.imageView.Source
                    = this.imgRes
                    = new Gray8Image(this.gray8ImgSrc).LogarithmicTransformation(c, v).ToBitmapSource();

            } else if(btn == buttonPointOps_Stretch) {
                var a = Common.String2Float(this.textBoxPointOps_Stretch_a.Text, 0.0f, 0.0f, 1.0f);
                var b = Common.String2Float(this.textBoxPointOps_Stretch_b.Text, 1.0f, 0.0f, 1.0f);
                if (a > b)
                    a = b - 1e-30f;
                this.textBoxPointOps_Stretch_a.Text = a.ToString();
                this.textBoxPointOps_Stretch_b.Text = b.ToString();

                this.imageView.Source 
                    = this.imgRes 
                    = new Gray8Image(this.gray8ImgSrc).ContrastStretching(a, b).ToBitmapSource();
            }

            UpdateHistogram();
        }


        /**************** 以下是用于控制滑动条的部分 ****************/
        
            /*
         * 参考资料：
         * 使 Slider 只在拖动结束才响应：
         * [WPF: Slider with an event that triggers after a user drags]
         * (http://stackoverflow.com/questions/723502/wpf-slider-with-an-event-that-triggers-after-a-user-drags)
         */

        private bool isSliderDraging = false;

        private void slider_Action(Slider slider)
        {
            if (slider == sliderQuantization)
                this.imageView.Source
                    = this.imgRes
                    = new Bgra32Image(this.bgraImgSrc).Quantize((uint)this.sliderQuantization.Value).ToBitmapSource();
            else if (slider == sliderSampling)
                this.imageView.Source 
                    = this.imgRes 
                    = new Bgra32Image(this.bgraImgSrc).Sample((uint)this.sliderSampling.Value).ToBitmapSource();
            else if (slider == sliderGray8)
                this.imageView.Source 
                    = this.imgRes 
                    = new Gray8Image(this.gray8ImgSrc).GrayProcessing((uint)this.sliderGray8.Value).ToBitmapSource();
            else if (slider == sliderThresholdMax || slider == sliderThresholdMin)
                this.imageView.Source 
                    = this.imgRes 
                    = new Gray8Image(this.gray8ImgSrc).Threshold((uint)this.sliderThresholdMin.Value, (uint)this.sliderThresholdMax.Value).ToBitmapSource();

            UpdateHistogram();
        }

        private void slider_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
        {

            if (!(e.Source is Slider) || this.imgSrc == null)
                return;

            var slider = sender as Slider;

            if (slider == sliderThresholdMax && sliderThresholdMax.Value < sliderThresholdMin.Value)
                sliderThresholdMin.Value = sliderThresholdMax.Value;
            if (slider == sliderThresholdMin && sliderThresholdMax.Value < sliderThresholdMin.Value)
                sliderThresholdMax.Value = sliderThresholdMin.Value;

            if (this.isSliderDraging && !realtimeProcessing)
                return;

            slider_Action(slider);
        }

        private void slider_DragStarted(object sender, RoutedEventArgs e)
        {
            if (!(e.Source is Slider) || this.imgSrc == null || realtimeProcessing)
                return;

            this.isSliderDraging = true;
        }

        private void slider_DragCompleted(object sender, RoutedEventArgs e)
        {
            if (!(e.Source is Slider) || this.imgSrc == null || realtimeProcessing)
                return;

            slider_Action(sender as Slider);
            this.isSliderDraging = false;
        }

        /**************** 以上是用于控制滑动条的部分 ****************/

        /**************** 以下是用于控制数字文本框的部分 ****************/

        /*
         *  [WPF文本框只允许输入数字](http://www.oschina.net/code/snippet_565270_10848)
         *  [How do I get a TextBox to only accept numeric input in WPF?](http://stackoverflow.com/questions/1268552/how-do-i-get-a-textbox-to-only-accept-numeric-input-in-wpf)
         */

        private void textBoxNumberOnly_Pasting(object sender, DataObjectPastingEventArgs e)
        {
            if (e.DataObject.GetDataPresent(typeof(string)))
            {
                var text = (string)e.DataObject.GetData(typeof(string));
                if (!isNumberic(text))
                    e.CancelCommand();
            }
            else
                e.CancelCommand();
        }

        private void textBoxNumberOnly_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Space)
                e.Handled = true;
        }

        private void textBoxNumberOnly_PreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            var regex = new Regex("[^0-9.-]+");
            e.Handled = regex.IsMatch(e.Text);
        }

        public static bool isNumberic(string str)
        {
            if (string.IsNullOrEmpty(str))
                return false;

            double num = 0;
            bool success = double.TryParse(str, out num);
            return success;
        }

        /**************** 以上是用于控制数字文本框的部分 ****************/


        /**************** 以下是 按钮被点击 的处理部分 ****************/
        private void buttonSave_Click(object sender, RoutedEventArgs e)
        {
            if (!(e.Source is Button) || this.imgSrc == null)
                return;

            Common.SaveImgFile(imgRes);
        }

        private void buttonShowText_Click(object sender, RoutedEventArgs e)
        {
            if (!(e.Source is Button) || this.imgTxt == null)
                return;

            /*
             * 关于在 C# 中打开一个临时文件：
             * [Open text file in notepad from C#](https://bytes.com/topic/c-sharp/answers/230427-open-text-file-notepad-c)
             * 在等待执行结束前进入阻塞状态：
             * [System.Diagnostics.Process.Start如何进入阻塞状态，等待返回？](http://bbs.csdn.net/topics/390081874)
             */

            var fileName = Common.CreateTmpTxtFile(imgTxt);
            if (File.Exists(fileName))
            {
                System.Diagnostics.Process.Start(fileName).WaitForExit();
                File.Delete(fileName);
            }
        }

        private void buttonSaveText_Click(object sender, RoutedEventArgs e)
        {
            if (!(e.Source is Button) || this.imgTxt == null)
                return;

            Common.SaveTxtFileWithDialog(imgTxt);
        }

        private void buttonHuffmanEncode_Click(object sender, RoutedEventArgs e)
        {
            if(!(e.Source is Button) || this.imgRes == null)
                return;

            Common.SaveImgFileWithHuffmanCoding(this.imgRes);
        }

        private void buttonHuffmanDecode_Click(object sender, RoutedEventArgs e)
        {
            if(!(e.Source is Button))
                return;

            var img = Common.OpenImgFileWithHuffmanCoding();
            if (img != null)
                LoadImg(this.imgSrc = img);
        }

        private void buttonHistogramFold_CLick(object sender, RoutedEventArgs e)
        {
            if (panelHistogram.Visibility == Visibility.Collapsed) {
                buttonHistogram.Content = "-";
                panelHistogram.Visibility = Visibility.Visible;
            } else {
                buttonHistogram.Content = "+";
                panelHistogram.Visibility = Visibility.Collapsed;
            }
        }
        /**************** 以上是 保存按钮被点击 的处理部分 ****************/

        /**************** 以下是 二值图 的处理部分 ****************/

        private void Button_Click_MorphologyExpand(object sender, RoutedEventArgs e)
        {
            this.imageView.Source
                = this.imgRes
                = new Gray8Image(this.imgRes).MorphologyExpand().ToBitmapSource();

            UpdateHistogram();
        }

        private void Button_Click_MorphologyCorrode(object sender, RoutedEventArgs e)
        {
            this.imageView.Source
                = this.imgRes
                = new Gray8Image(this.imgRes).MorphologyCorrode().ToBitmapSource();

            UpdateHistogram();
        }

        /**************** 以上是 二值图 的处理部分 ****************/

        /**************** 以下是 频域滤波 的处理部分 ****************/
        int filter_matrix_wid = 3;
        int filter_matrix_hgt = 3;
        double[,] filter_matrix = new double[3, 3];

        private void buttonFilter_Click(object sender, RoutedEventArgs e)
        {
            if(!(e.Source is Button))
                return;

            if(e.Source == button_FilterIncreaseCol) {
                filter_matrix_wid += 2;
                if(filter_matrix_wid > 1)
                    button_FilterReduceCol.IsEnabled = true;
            } else if(e.Source == button_FilterReduceCol) {
                filter_matrix_wid -= 2;
                if(filter_matrix_wid <= 1)
                    button_FilterReduceCol.IsEnabled = false;
            } else if(e.Source == button_FilterIncreaseRow) {
                filter_matrix_hgt += 2;
                if(filter_matrix_hgt > 1)
                    button_FilterReduceRow.IsEnabled = true;
            } else if(e.Source == button_FilterReduceRow) {
                filter_matrix_hgt -= 2;
                if(filter_matrix_hgt <= 1)
                    button_FilterReduceRow.IsEnabled = false;
            }

            filter_matrix = ImgFunc.ResizeArray2D(filter_matrix, filter_matrix_wid, filter_matrix_hgt);

            datagrid_FilterMatrix.ItemsSource = ImgFunc.ConvertArray2DToDataTable(filter_matrix).AsDataView();
        }

        private void buttonFilterCalc_Click(object sender, RoutedEventArgs e)
        {
            this.imageView.Source
                = this.imgRes
                = new Bgra32Image(this.bgraImgSrc).Filter(filter_matrix).ToBitmapSource();

            UpdateHistogram();
        }

        private void datagrid_FilterMatrix_CurrentCellChanged(object sender, EventArgs e)
        {
            /* 
             * [Convert and use DataTable in WPF DataGrid?]
             * (http://stackoverflow.com/questions/6984686/convert-and-use-datatable-in-wpf-datagrid)
             */

            var table = ((DataView)datagrid_FilterMatrix.ItemsSource).ToTable();
            filter_matrix = ImgFunc.ConvertDataTableToArray2D<double>(table);

            if(realtimeProcessing)
                buttonFilterCalc_Click(null, null);
        }

        private void comboBox_Filter_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if(e.Source == comboBox_Filter) {
                switch(comboBox_Filter.SelectedIndex) {
                case 0: { filter_matrix = new double[,] { 
                    {  0, -1,  0 },
                    { -1,  4, -1 },
                    {  0, -1,  0 },
                }; break; }
                case 1: { filter_matrix = new double[,] { 
                    {1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0 },
                    {1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0 },
                    {1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0 },
                }; break; }
                case 2: {filter_matrix = new double[,] {
                    {1 / 10.0, 1 / 10.0, 1 / 10.0 },
                    {1 / 10.0, 2 / 10.0, 1 / 10.0 },
                    {1 / 10.0, 1 / 10.0, 1 / 10.0 },
                }; break;}
                case 3: {filter_matrix = new double[,] {
                    {1 / 16.0, 1 / 8.0, 1 / 16.0 },
                    {1 /  8.0, 1 / 4.0, 1 /  8.0 },
                    {1 / 16.0, 1 / 8.0, 1 / 16.0 },
                }; break;}
                case 4: {filter_matrix = new double[,] {
                    {  0, -1,  0 },
                    { -1,  5, -1 },
                    {  0, -1,  0 },
                }; break;}
                case 5: {filter_matrix = new double[,] {
                    { -1, -1, -1 },
                    { -1,  9, -1 },
                    { -1, -1, -1 },
                }; break;}
                case 6: {filter_matrix = new double[,] {
                    {  1, -2,  1 },
                    { -2,  5, -2 },
                    {  1, -2,  1 },
                }; break;}
                case 7: {filter_matrix = new double[,] {
                    {  0,  0,  0 },
                    { -1,  1,  0 },
                    {  0,  0,  0 },
                }; break;}
                case 8: {filter_matrix = new double[,] {
                    {  0, -1,  0 },
                    {  0,  1,  0 },
                    {  0,  0,  0 },
                }; break;}
                case 9: {filter_matrix = new double[,] {
                    { -1,  0,  0 },
                    {  0,  1,  0 },
                    {  0,  0,  0 },
                }; break;}
                case 10: {filter_matrix = new double[,] {
                    { -1, -1, -1, -1, -1 },
                    {  0,  0,  0,  0,  0 },
                    {  1,  1,  1,  1,  1 },
                }; break;}
                case 11: {filter_matrix = new double[,] {
                    { -1, 0, 1 },
                    { -1, 0, 1 },
                    { -1, 0, 1 },
                    { -1, 0, 1 },
                    { -1, 0, 1 },
                }; break;}
                case 12: {filter_matrix = new double[,] {
                    { -1,  0, -1 },
                    {  0,  4,  0 },
                    { -1,  0, -1 },
                }; break;}
                case 13: {filter_matrix = new double[,] {
                    { -1, -1, -1 },
                    { -1,  8, -1 },
                    { -1, -1, -1 },
                }; break;}
                case 14: {filter_matrix = new double[,] {
                    { -1, -1, -1 },
                    { -1,  9, -1 },
                    { -1, -1, -1 },
                }; break;}
                case 15: {filter_matrix = new double[,] {
                    {  1, -2, -1 },
                    { -2,  4, -2 },
                    {  1, -2, -1 },
                }; break;}
                default: break;
                }

                datagrid_FilterMatrix.ItemsSource = ImgFunc.ConvertArray2DToDataTable(filter_matrix).AsDataView();
                if(realtimeProcessing)
                    buttonFilterCalc_Click(null, null);
            } else if(e.Source == comboBox_FilterOther) {
                BitmapSource img = null;
                switch(comboBox_FilterOther.SelectedIndex) {
                case 0: img = new Bgra32Image(this.bgraImgSrc).Filter("median").ToBitmapSource(); break;
                case 1: img = new Bgra32Image(this.bgraImgSrc).Filter("roberts").ToBitmapSource(); break;
                case 2: img = new Bgra32Image(this.bgraImgSrc).Filter("sobel").ToBitmapSource(); break;
                }
                if(img != null)
                    this.imageView.Source = this.imgRes = img;
            }
        }

        /**************** 以上是 频域滤波 的处理部分 ****************/

        /**************** 以下是 选项卡切换选项 的处理部分 ****************/

        /*
         * 参考资料：
         * 响应，切换选项卡：
         * [Is there Selected Tab Changed Event in the standard WPF Tab Control](http://stackoverflow.com/questions/772841/is-there-selected-tab-changed-event-in-the-standard-wpf-tab-control)
         */

        private void tabControl_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (!(e.Source is TabControl) || this.imgSrc == null)
                return;

            if (tabItem_Origin.IsSelected)
                OriginImg(this.imgSrc);
            else if (tabItem_Quantization.IsSelected)
                this.imageView.Source
                    = this.imgRes
                    = new Bgra32Image(this.bgraImgSrc).Quantize((uint)this.sliderQuantization.Value).ToBitmapSource();
            else if (tabItem_Sampling.IsSelected)
                this.imageView.Source
                    = this.imgRes
                    = new Bgra32Image(this.bgraImgSrc).Sample((uint)this.sliderSampling.Value).ToBitmapSource();
            else if (tabItem_Binary.IsSelected)
                this.imageView.Source
                    = this.imgRes
                    = new Gray8Image(this.gray8ImgSrc).GrayProcessing((uint)this.sliderGray8.Value).ToBitmapSource();
            else if (tabItem_Img2txt.IsSelected)
                this.imgTxt = ImgFunc.Bitmap2string(this.imgRes);
            else if (tabItem_PointOps.IsSelected)
                this.imageView.Source
                    = this.imgRes
                    = new Gray8Image(this.gray8ImgSrc).GrayProcessing(0).ToBitmapSource();
            else if (tabItem_HistogramEqualization.IsSelected)
                this.imageView.Source
                    = this.imgRes
                    = new Bgra32Image(this.bgraImgSrc).HistogramEqualization().ToBitmapSource();
            else if (tabItem_Transform.IsSelected)
                this.TransformReset();
            else if (tabItem_Filter.IsSelected) {
                /* 更新 数据表格 */
                datagrid_FilterMatrix.ItemsSource = ImgFunc.ConvertArray2DToDataTable(filter_matrix).AsDataView();
            }
            
            UpdateHistogram();
        }

        /**************** 以上是 选项卡切换选项 的处理部分 ****************/
 

        /**************** 以下是 鼠标平移、旋转、缩放图片的 的处理部分 ****************/
        private double transform_OldAngle = 0.0;
        private double transform_OldScale = 1.0;
        private double transform_OldDeltaX = 0.0;
        private double transform_OldDeltaY = 0.0;
        Point transform_OldLocation;
        bool transform_MouseLeftPressed = false;
        bool transform_MouseRightPressed = false;

        /* 
         * [How to bind RadioButtons to an enum?](http://stackoverflow.com/questions/397556/how-to-bind-radiobuttons-to-an-enum)
         */

        private void TransformReset()
        {
            transform_OldAngle = 0.0;
            transform_OldScale = 1.0;
            transform_OldDeltaX = 0.0;
            transform_OldDeltaY = 0.0;
            transform_MouseLeftPressed = false;
            transform_MouseRightPressed = false;
        }

        private void TransformDo(Point point)
        {
            double dx = transform_OldDeltaX;
            double dy = transform_OldDeltaY;
            if (transform_MouseLeftPressed) {
                dx += point.X - transform_OldLocation.X;
                dy -= point.Y - transform_OldLocation.Y;
            }

            /*
             * [Getting current size of WPF controls](http://stackoverflow.com/questions/7903306/getting-current-size-of-wpf-controls)
             */

            dx *= imgSrc.PixelWidth / imageView.ActualWidth;
            dy *= imgSrc.PixelHeight / imageView.ActualHeight;

            double angle = transform_OldAngle;
            double scale = transform_OldScale;
            if (transform_MouseRightPressed) {
                double delta_scale, delta_angle;
                TransformVector2Info(transform_OldLocation, point, out delta_scale, out delta_angle);
                scale *= delta_scale;
                angle += delta_angle;
            }

            if(radio_Transform_Bilinear.IsChecked == true)
                this.imageView.Source
                    = this.imgRes
                    = new Bgra32Image(this.bgraImgSrc).BilinearTransform(dx, dy, angle, scale).ToBitmapSource();
            else if(radio_Transform_neighbor.IsChecked == true)
                this.imageView.Source
                    = this.imgRes
                    = new Bgra32Image(this.bgraImgSrc).NeighborTransform(dx, dy, angle, scale).ToBitmapSource();

            UpdateHistogram();
        }

        private void TransformVector2Info(Point point0, Point point1, out double delta_scale, out double delta_angle)
        {
            var center_x = this.ActualWidth * 0.5 + transform_OldDeltaX;
            var center_y = this.ActualHeight * 0.5 - transform_OldDeltaY;
            
            var x0 = point0.X - center_x;
            var y0 = center_y - point0.Y;
            var x1 = point1.X - center_x;
            var y1 = center_y - point1.Y;

            delta_scale = Math.Sqrt((x1 * x1 + y1 * y1) / (x0 * x0 + y0 * y0));
            delta_angle = Math.Atan2(y0, x0) - Math.Atan2(y1, x1);
        }

        private void imageView_MouseMove(object sender, MouseEventArgs e)
        {
            if (!(sender is Image) || !tabItem_Transform.IsSelected)
                return;

            if (realtimeProcessing && (transform_MouseLeftPressed || transform_MouseRightPressed))
                TransformDo(e.GetPosition(this));
        }

        private void imageView_MouseButton(object sender, MouseButtonEventArgs e)
        {
            if (!(sender is Image) || !tabItem_Transform.IsSelected)
                return;

            /* [How to get mouse position on screen in WPF?]
             * (http://stackoverflow.com/questions/29822020/how-to-get-mouse-position-on-screen-in-wpf)
             */

            var point = e.GetPosition(this);

            if (e.ChangedButton == MouseButton.Left) {
                if (e.ButtonState == MouseButtonState.Pressed) {
                    transform_OldLocation = point;
                    transform_MouseLeftPressed = true;
                } else {
                    TransformDo(point);

                    /* 更新一次平移数据 */
                    transform_OldDeltaX += point.X - transform_OldLocation.X;
                    transform_OldDeltaY -= point.Y - transform_OldLocation.Y;
                    transform_MouseLeftPressed = false;
                }
            } else if (e.ChangedButton == MouseButton.Right) {
                if (e.ButtonState == MouseButtonState.Pressed) {
                    transform_OldLocation = point;
                    transform_MouseRightPressed = true;
                } else {
                    TransformDo(point);

                    /* 更新一次缩放、旋转数据 */
                    double delta_scale, delta_angle;
                    this.TransformVector2Info(transform_OldLocation, point, out delta_scale, out delta_angle);
                    transform_OldScale *= delta_scale;
                    transform_OldAngle += delta_angle;
                    transform_MouseRightPressed = false;
                }
            }
        }

        /**************** 以上是 鼠标旋转、缩放图片的 的处理部分 ****************/


        /**************** 以下是 更新显示的图片 的处理部分 ****************/

        private void UpdateHistogram()
        {
            /* 刷新直方图 */
            ImageSource histogram = null;
            int mean, mid, sd, sum;
            new Gray8Image(this.imgRes).Histogram(out histogram, out mean, out mid, out sd, out sum);

            this.textHistogram_mean.Text = mean.ToString();
            this.textHistogram_mid.Text = mid.ToString();
            this.textHistogram_sd.Text = sd.ToString();
            this.textHistogram_sum.Text = sum.ToString();
            this.imageHistogram.Source = histogram;
        }

        private void OriginImg(BitmapSource img)
        {
            /* 设定主窗口显示的图片 */
            this.imageView.Source = imgRes = img;

            /* 强制跳转 */
            tabItem_Origin.IsSelected = true;
        }

        private void ResizeForImg(BitmapSource img)
        {
            /* 设定图片窗口为图片原本大小，如果超过设定上限则按比例缩放 */
            var wid = (double)img.PixelWidth;
            var hgt = (double)img.PixelHeight;
            if (hgt > imgHgtMax || wid > imgWidMax)
            {
                if (hgt > imgHgtMax && wid > imgWidMax) {
                    if (hgt * imgWidMax > imgHgtMax * wid) {
                        wid = wid / hgt * imgHgtMax;
                        hgt = imgHgtMax;
                    } else {
                        hgt = hgt / wid * imgWidMax;
                        wid = imgWidMax;
                    }
                } else if (hgt > imgHgtMax) {
                    wid = wid / hgt * imgHgtMax;
                    hgt = imgHgtMax;
                } else {
                    hgt = hgt / wid * imgWidMax;
                    wid = imgWidMax;
                }
            }

            /* 非全屏模式下会自动调整窗口大小 */
            if (this.WindowState != WindowState.Maximized && this.WindowState != WindowState.Minimized)
            {
                /* 
                 * 笔记：
                 * 尝试多次终于对了……实现了载入图片时使之调整为不缩放的状态
                 * 其它：关于 `Auto`
                 * [WPF Auto height in code](http://stackoverflow.com/questions/2459089/wpf-auto-height-in-code) 
                 */
                this.imageView.Height = hgt;
                this.imageView.Width = wid;

                /* 防止 tabControl 对窗口大小的影响 */
                this.tabControl.Width = 0;

                /* 设定窗口大小为“适应内容”。更新一次布局，使“适应内容”的更改生效。再改回“手动调节”的模式 */
                this.SizeToContent = SizeToContent.WidthAndHeight;
                this.UpdateLayout();
                this.SizeToContent = SizeToContent.Manual;

                /* 将图片设定为适应窗口大小，这样图片就可以随“手动调节”缩放了 */

                this.tabControl.Width = double.NaN;

                this.imageView.Height = double.NaN;
                this.imageView.Width = double.NaN;
            }

        }
        
        private void EnbaleComponent()
        {
            this.buttonSave.IsEnabled = true;

            this.tabItem_Binary.IsEnabled = true;
            this.tabItem_Img2txt.IsEnabled = true;
            this.tabItem_Origin.IsEnabled = true;
            this.tabItem_PointOps.IsEnabled = true;
            this.tabItem_Quantization.IsEnabled = true;
            this.tabItem_Sampling.IsEnabled = true;
            this.tabItem_HistogramEqualization.IsEnabled = true;
            this.tabItem_Transform.IsEnabled = true;
            this.tabItem_Filter.IsEnabled = true;
            this.tabItem_Coding.IsEnabled = true;

            // 对应 XAML 代码
            //< Window.Background >
            //    < ImageBrush ImageSource = "img/background.png" TileMode = "Tile" ViewportUnits = "Absolute" Viewport = "0,0,32,32" Stretch = "UniformToFill" />
            //</ Window.Background >

            /*
             * 关于 WPF 资源路径写法：
             * [WPF image resources](http://stackoverflow.com/questions/347614/wpf-image-resources)
             */

            this.Background = new ImageBrush()
            {
                ImageSource = new BitmapImage(new Uri(@"pack://application:,,,/" + Assembly.GetExecutingAssembly().GetName().Name + ";component/img/background.png", UriKind.Absolute)),
                TileMode = TileMode.Tile,
                ViewportUnits = BrushMappingMode.Absolute,
                Viewport = new Rect(0, 0, 32, 32),
                Stretch = Stretch.UniformToFill
            };
        }

        private void LoadImg(BitmapSource img)
        {
            /*
             * 笔记：
             *
             * 最开始尝试使用 System.drawing 实现转换为 byte，想了想还是算了（各种转化），这些是当时的资料
             * 参考资料：
             * [Convert a bitmap into a byte array](http://stackoverflow.com/questions/7350679/convert-a-bitmap-into-a-byte-array?lq=1)
             * [System.drawing namespace not found under console application](http://stackoverflow.com/questions/8553136/system-drawing-namespace-not-found-under-console-application)
             *
             * 最后决定还是直接转换 BitmapImage 到 byte[]
             *
             * 然后翻了一下这个 [BitmapImage to byte[]](http://stackoverflow.com/questions/6597676/bitmapimage-to-byte)
             * （虽然最后并没有用上，但是知道了如何转换 BitmapImage 的格式）
             *
             * 最后，参考资料：
             * [Finding specific pixel colors of a BitmapImage](http://stackoverflow.com/questions/1176910/finding-specific-pixel-colors-of-a-bitmapimage)
             * 这里强行把图片换成了 PixelFormats.Bgra32，同时使用 CopyPixels 获取各个像素信息到 byte[] 中，
             * 这样就可以交给外部方法处理了。
             */

            /* 保存 Bgra32 版本的像素 */
            bgraImgSrc = new Bgra32Image(img);
            //BgraImgNow = new Bgra32Image(BgraImgSrc);

            /* 保存 8bit 灰度图的版本 */
            gray8ImgSrc = new Gray8Image(img);

            /* 根据图片大小，确定是否实时计算 */
            realtimeProcessing = (img.PixelWidth * img.PixelHeight) < (imgWidMax * imgHgtMax);

            /* 确定采样的上限值 */
            sliderSampling.Maximum = Math.Max(img.PixelWidth, img.PixelHeight);

            /* 调整窗口大小 */
            ResizeForImg(this.imgSrc);

            /* 显示原图像 */
            OriginImg(this.imgSrc);

            /* 启用 保存 按钮 */
            try {
                if(ImgFunc.is_loaded())
                    EnbaleComponent();

                /* 刷新直方图 */
                UpdateHistogram();
            } catch(Exception e) {
                MessageBox.Show(
                    e.Message,
                    "加载 ImgFuncLib 时发生错误",
                    MessageBoxButton.OK,
                    MessageBoxImage.Error
                    );
            }
        }

        private Bgra32Image bgraImgSrc;         /* 原始图像 */
        private Gray8Image gray8ImgSrc;         /* 原始图像的灰度图版本 */
        private bool realtimeProcessing;        /* 实时处理 */
        private BitmapSource imgRes;            /* 记录处理后的图像的 BitmapSource */
        private BitmapSource imgSrc;            /* 记录原始图像的 BitmapSource */
        private string imgTxt;                  /* 转换为 字符串 的图片 */
        private const double imgWidMax = 1280;  /* 图像最大宽度，超过时，将会在显示框中缩放 */
        private const double imgHgtMax = 800;   /* 图像最大高度，超过时，将会在显示框中缩放 */

    }

}



/*
 * 其它资料：
 * 
 * 为按钮添加 alt 激活（出现一个下划线）的快捷键
 * [Set short cut keys for a button in WPF/XAML](http://forums.codeguru.com/showthread.php?495856-Set-short-cut-keys-for-a-button-in-WPF-XAML)
 *
 */
