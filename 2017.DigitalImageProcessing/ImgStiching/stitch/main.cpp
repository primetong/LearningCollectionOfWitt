#include <iostream>
#include <opencv2/opencv.hpp>

bool stitch()
{
    std::vector<cv::String> filenames = {
        "building01.jpg",
        "building04.jpg",
        "building02.jpg",
        "building03.jpg",
    };

    std::vector<cv::Mat> src;
    for (auto & name : filenames) {
        cv::Mat img = cv::imread(name, cv::IMREAD_REDUCED_COLOR_4);
        if (img.empty())
            return(std::cout<<"failed to load '"<<name<<"'!\n", false);
        src.push_back(img);
    }

    cv::Mat dst;
    cv::Stitcher stitcher = cv::Stitcher::createDefault(true);
    cv::Stitcher::Status status = stitcher.stitch(src, dst);

    if (status != cv::Stitcher::OK)
        return(std::cout<<"failed to stitch("<<status<<")!\n", false);

    cv::imshow("Result", dst);
    return(cv::waitKey(), true);
}

int main()
{
    return static_cast<int>(stitch());
}
