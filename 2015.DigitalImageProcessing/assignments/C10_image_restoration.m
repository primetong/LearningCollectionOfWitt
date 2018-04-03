% 使用MATLAB相关图像复原的函数，
% 针对以下图像编写图像复原的程序，
% 重点观察不同参数对图像复原的影响。

I = imread('image_restoration.png');

G = fspecial('motion', 10, 160);     % （运动模糊算子，运动的像素点距离，角度）
F = imfilter(I, G);

figure('Name', '处理结果', 'NumberTitle', 'off');
imshow(F);
