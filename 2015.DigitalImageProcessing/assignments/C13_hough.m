% ���û���任���ͼ�е�ֱ�ߺ�Բ


% ��ȡͼƬ��ת��Ϊ�Ҷ�ͼ
I = imread('hough.png');
if (size(I,3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);
figure('Name','����任','NumberTitle','off');
subplot(121);
title('ԭʼͼ��');
imshow(I);


%%%%%%%%%
% Hough %
%%%%%%%%%
[H, T, R] = hough(1 - I, 'RhoResolution', 0.5);

% ����Ϊ��ֵ
peak = houghpeaks(H, 2);
lines = houghlines(1 - I, T, R, peak);

subplot(122);
title('���');
imshow(I);
hold on;

for i = 1 : length(lines)
   l = [lines(i).point1; lines(i).point2];
   plot(l(:,1), l(:,2), 'LineWidth', 4, 'Color', [.8 .1 .1]);
end

