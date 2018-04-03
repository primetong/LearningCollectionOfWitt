#include <numeric>
#include <functional>
#include <iostream>
#include <opencv2/opencv.hpp>

int main()
{
    auto videoname = "video.simple.mp4";
    cv::VideoCapture cap(videoname); {
        if (!cap.isOpened())
            return (std::cout<<"Cannot open "<<videoname<<"."<<std::endl, 1);
    }

    auto haarcascade = "haarcascade_frontalface_default.xml";
    cv::CascadeClassifier classifier(haarcascade); {
        if (classifier.empty())
            return(std::cout<<"failed to load:"<<haarcascade<<"!", 2);
    }

    cv::Mat src, dst;
    cv::namedWindow("Video");
    while (cap.read(src) && cv::waitKey(10) < 0)
    {
        // 查询人脸的中心位置
        std::vector<cv::Point> points;
        {
            std::vector<cv::Rect> rects;
            classifier.detectMultiScale(src, rects);
            points.reserve(rects.size());
            for (auto & rect : rects) {
                points.emplace_back((rect.br() + rect.tl()) / 2);                
            }
        }

        // 转换图像类型，方便进行二值化
        src.convertTo(dst, CV_8UC3);
        cv::cvtColor(dst, dst, cv::COLOR_BGR2GRAY);

        // 减少噪声并二值化
        cv::GaussianBlur(dst, dst, cv::Size(3, 3), 0.0);
        cv::threshold(dst, dst, 192.0, 255.0, cv::THRESH_BINARY_INV);

        // 膨胀和腐蚀，进一步消除噪点
        cv::dilate(dst, dst, cv::getStructuringElement(cv::MORPH_RECT, cv::Size(3, 3)));
        cv::erode(dst, dst, cv::getStructuringElement(cv::MORPH_RECT, cv::Size(5, 5)));

        // 删除不符合要求的连通域
        {
            std::vector<std::vector<cv::Point> > contours;
            std::vector<std::vector<cv::Point> > refs;
            cv::findContours(dst, contours, cv::RETR_EXTERNAL, cv::CHAIN_APPROX_SIMPLE);
            for (auto & contour : contours) {
                auto it = std::find_if(points.begin(), points.end(), [&](auto & it) {
                    return cv::pointPolygonTest(contour, it, false) > 0.0;
                });
                if (it != points.end())
                    refs.emplace_back(std::ref(contour));
            }

            cv::Mat msk = cv::Mat::zeros(dst.size(), CV_8U);
            cv::drawContours(msk, refs, -1, cv::Scalar::all(255), cv::FILLED);
            src.copyTo(dst, msk);
        }

        cv::imshow("Video", dst);
    }

    cv::destroyAllWindows();
    return 0;
}


// [Reading and Writing Video]
// (https://docs.opencv.org/3.0-beta/modules/videoio/doc/reading_and_writing_video.html)
// [Canny Edge Detector]
// (https://docs.opencv.org/2.4.13.4/doc/tutorials/imgproc/imgtrans/canny_detector/canny_detector.html)
// [Zero-parameter, automatic Canny edge detection with Python and OpenCV]
// (https://www.pyimagesearch.com/2015/04/06/zero-parameter-automatic-canny-edge-detection-with-python-and-opencv/)
// [OpenCV 2.4 VideoCapture not working on Windows]
// (https://stackoverflow.com/a/11703998)
// [Point Polygon Test]
// (https://docs.opencv.org/3.2.0/dc/d48/tutorial_point_polygon_test.html)