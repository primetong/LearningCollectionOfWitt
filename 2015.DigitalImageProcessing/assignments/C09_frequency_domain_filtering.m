% 利用MATLAB或其他快速开发平台，编写频域滤波程序。

% 获取输入
I = imread('cameraman.tif');
if (size(I, 3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);

figure('Name','频域滤波','NumberTitle','off');
subplot(221);
imshow(I);
title('原始图像');

F = fft2(I);        % 对灰度图像进行 FFT
F = fftshift(F);    % 将傅里叶变化的图形移到图像中心对称的位置

%%%%%%%%%%%%%%%%%%%%%%%
% Butterworth 低通滤波 %
%%%%%%%%%%%%%%%%%%%%%%%
a = 40;
b = 40;

[m, n] = size(F);
i0 = fix(m / 2);
j0 = fix(n / 2);
G = I;
for i = 1 : m
    for j = 1 : n
        d = sqrt((i - i0)^2 + (j - j0)^2);
        G(i,j) = F(i, j) /(1 + (d / a)^(2 * b));
    end
end

%从频域转化回空域
G = real(ifft2(ifftshift(G)));
subplot(223)
imshow(G);
title('Butterworth 低通滤波');

%%%%%%%%%%%%%%%
% 高通维纳滤波 %
%%%%%%%%%%%%%%%
k = 0.00025;
[m, n] = size(I);
G = zeros(m,n);
H = zeros(m,n);
for i = 1 : m
      for j = 1 : n
       H(i,j) = exp(-k * ((i - m / 2)^2 + (j - n / 2)^2)^(5 / 6));
       G(i,j) = H(i, j)^2;
      end
end
HW = H ./ (G + 0.0001);
O = HW .* F;
 
%从频域转化回空域
O = real(ifft2(ifftshift(O)));

subplot(224);
imshow(O);
title('高通维纳滤波');
