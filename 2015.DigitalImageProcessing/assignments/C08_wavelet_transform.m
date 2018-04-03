% 请使用 MATLAB 或 OPENCV 等工具完成各种图像变换的演示，
% 要求实现频谱图显示，反变换重构结果显示。
% 应包括 傅里叶变换、离散余弦变换 和 小波变换。

% 获取输入
I = imread('cameraman.tif');
if (size(I,3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);
figure('Name','原始图像','NumberTitle','off');
imshow(I);

%%%%%%%
% FFT %
%%%%%%%
F = fft2(I);                                % 对灰度图像进行 FFT
F = fftshift(F);                            % 将傅里叶变化的图形移到图像中心对称的位置
O = log(1 + abs(F));                        % 对变换结果取对数（为了让结果范围降低为 [0,255]，方便显示）

figure('Name','FFT','NumberTitle','off');
subplot(121);
imshow(O, []);
title('灰度图像的 FFT 频谱');

F = ifftshift(F);                           % 去中心化
F = ifft2(F);                               % 执行 FFT 的逆变换
subplot(122);
imshow(F);
title('FFT 逆变换处理结果'); 

%%%%%%%
% DCT %
%%%%%%%
F = dct2(I);                                % 对单通道图像 V 执行 DCT
O = log(abs(F));                            % 对变换结果取绝对值和对数（为了让结果范围降低为 [0,255]，方便显示）

figure('Name','DCT','NumberTitle','off');
subplot(121);
imshow(O, []);
title('灰度图的 DCT 频谱');

F(abs(F)<0.04) = 0;                         % 似乎是为了减小噪声误差
F = idct2(F);                               % 执行 idct 逆变换

subplot(122);
imshow(F);
title('DCT 逆变换结果');

%%%%%%%
% DWT %
%%%%%%%
% PPT：08-58
s = size(I);
[cA1, cH1, cV1, cD1] = dwt2(I,'bior3.7');   % 开始 DWT，这里和 PPT 给的代码一样了
A1 = upcoef2('a',cA1,'bior3.7',1);
H1 = upcoef2('h',cH1,'bior3.7',1);
V1 = upcoef2('v',cV1,'bior3.7',1);
D1 = upcoef2('d',cD1,'bior3.7',1);

figure('Name','DWT','NumberTitle','off');   % 新建窗口，挨着显示每个通道
subplot(231);
image(wcodemat(A1,192));
title('Approximation A1')
subplot(232);
image(wcodemat(H1,192));
title('Horizontal Detail H1')
subplot(233);
image(wcodemat(V1,192));
title('Vertical Detail V1')
subplot(234);
image(wcodemat(D1,192));
title('Diagonal Detail D1')

F = idwt2(cA1,cH1,cV1,cD1,'bior3.7',s);
subplot(235);
imshow(F);
title('DWT 逆变换处理结果');

