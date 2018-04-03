% 利用霍夫变换检测图中的直线和圆


% 读取图片并转换为灰度图
I = imread('hough.png');
if (size(I,3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);
figure('Name','霍夫变换','NumberTitle','off');
subplot(121);
title('原始图像');
imshow(I);


%%%%%%%%%
% Hough %
%%%%%%%%%
[H, T, R] = hough(1 - I, 'RhoResolution', 0.5);

% 测线为峰值
peak = houghpeaks(H, 2);
lines = houghlines(1 - I, T, R, peak);

subplot(122);
title('结果');
imshow(I);
hold on;

for i = 1 : length(lines)
   l = [lines(i).point1; lines(i).point2];
   plot(l(:,1), l(:,2), 'LineWidth', 4, 'Color', [.8 .1 .1]);
end

