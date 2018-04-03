using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Windows;
using Unosquare.FFME;
using System.Xml;

namespace multiplay
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            MediaElement.FFmpegDirectory = @".\ffmpeg\";
        }
         
        private void OnLoaded(object sender, RoutedEventArgs e)
        {
            XmlDocument doc = new XmlDocument();
            try
            {
                doc.Load(@"urls.xml");
                var urls = doc
                    .SelectNodes("/urls/url")
                    .OfType<XmlNode>()
                    .Select(node => node.InnerText)
                    ;

                FillGrid(urls);
            }
            catch(Exception)
            {
                // Ignore xml
            }
        }

        private void OnDrop(object sender, DragEventArgs e)
        {
            if (e.Data.GetDataPresent(DataFormats.FileDrop))
            {
                var urls = (e.Data.GetData(DataFormats.FileDrop) as string[]);
                FillGrid(urls);
            }
        }

        private void FillGrid(IEnumerable<string> urls)
        {
            var count = urls.Count();
            if (count > 0)
            {
                mediaGrid.Children.Clear();
                mediaGrid.Rows = (int)Math.Floor(Math.Sqrt(count));
                mediaGrid.Columns = 1 + (count - 1) / mediaGrid.Rows;

                foreach (var url in urls.Select((path, index) => new { path, index }))
                {
                    var media = new MediaElement()
                    {
                        LoadedBehavior = System.Windows.Controls.MediaState.Play,
                        UnloadedBehavior = System.Windows.Controls.MediaState.Manual
                    };
                    
                    try
                    {
                        if (url.index == 0)
                            media.MediaOpened += OnMediaOpened;
                        media.Source = new Uri(url.path);
                    }
                    catch (Exception e)
                    {
                        MessageBox.Show(
                            $"Media Failed: {e.GetType()}\r\n{e.Message}",
                            "MediaElement Error",
                            MessageBoxButton.OK,
                            MessageBoxImage.Error,
                            MessageBoxResult.OK);
                    }

                    mediaGrid.Children.Add(media);
                }
            }
        }

        private void OnMediaOpened(object sender, RoutedEventArgs e)
        {
            if (sender is MediaElement &&
                WindowState != WindowState.Maximized &&
                WindowState != WindowState.Minimized)
            {
                var medias = mediaGrid.Children.OfType<MediaElement>().ToList();
                var first = medias.First();
                var h = (first.NaturalVideoHeight * mediaGrid.ActualWidth)
                      / (first.NaturalVideoWidth * mediaGrid.Columns)
                      ;
                
                medias.ForEach(m => m.Height = h);
                SizeToContent = SizeToContent.WidthAndHeight;
                UpdateLayout();
                SizeToContent = SizeToContent.Manual;
                medias.ForEach(m => m.Height = double.NaN);
            }
        }
    }
}
