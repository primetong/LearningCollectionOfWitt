#include <iostream>
#include <opencv2/opencv.hpp>

bool detect()
{
    cv::String filename = "lena.jpg";
    cv::String dataname = "haarcascade_frontalface_alt2.xml";

    cv::CascadeClassifier cc(dataname);
    {
        if (cc.empty())
            return(std::cout<<"failed to load:"<<dataname<<"!", false);
    }

    cv::Mat src = cv::imread(filename, cv::IMREAD_COLOR), dst;
    {
        if (src.empty())
            return(std::cout<<"failed to load:"<<filename<<"!", false);

        cv::cvtColor    (src, dst, cv::COLOR_BGR2GRAY);
        cv::equalizeHist(dst, dst);
    }

    std::vector<cv::Rect> res;
    {
        cc.detectMultiScale(dst, res, 1.2, 6);
    }

    cv::imshow("Source Image", src);

    for (auto const & r : res) {
        // cv::rectangle(src, r, cv::Scalar(255.0), 1);
        cv::imshow("Result Image", src(r));
    }
    
    return(cv::waitKey(), true);
}

int main()
{
    detect();
    return 0;
}
