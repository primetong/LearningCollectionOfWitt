% ��ʹ�� MATLAB �� OPENCV �ȹ�����ɸ���ͼ��任����ʾ��
% Ҫ��ʵ��Ƶ��ͼ��ʾ�����任�ع������ʾ��
% Ӧ���� ����Ҷ�任����ɢ���ұ任 �� С���任��

% ��ȡ����
I = imread('cameraman.tif');
if (size(I,3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);
figure('Name','ԭʼͼ��','NumberTitle','off');
imshow(I);

%%%%%%%
% FFT %
%%%%%%%
F = fft2(I);                                % �ԻҶ�ͼ����� FFT
F = fftshift(F);                            % ������Ҷ�仯��ͼ���Ƶ�ͼ�����ĶԳƵ�λ��
O = log(1 + abs(F));                        % �Ա任���ȡ������Ϊ���ý����Χ����Ϊ [0,255]��������ʾ��

figure('Name','FFT','NumberTitle','off');
subplot(121);
imshow(O, []);
title('�Ҷ�ͼ��� FFT Ƶ��');

F = ifftshift(F);                           % ȥ���Ļ�
F = ifft2(F);                               % ִ�� FFT ����任
subplot(122);
imshow(F);
title('FFT ��任������'); 

%%%%%%%
% DCT %
%%%%%%%
F = dct2(I);                                % �Ե�ͨ��ͼ�� V ִ�� DCT
O = log(abs(F));                            % �Ա任���ȡ����ֵ�Ͷ�����Ϊ���ý����Χ����Ϊ [0,255]��������ʾ��

figure('Name','DCT','NumberTitle','off');
subplot(121);
imshow(O, []);
title('�Ҷ�ͼ�� DCT Ƶ��');

F(abs(F)<0.04) = 0;                         % �ƺ���Ϊ�˼�С�������
F = idct2(F);                               % ִ�� idct ��任

subplot(122);
imshow(F);
title('DCT ��任���');

%%%%%%%
% DWT %
%%%%%%%
% PPT��08-58
s = size(I);
[cA1, cH1, cV1, cD1] = dwt2(I,'bior3.7');   % ��ʼ DWT������� PPT ���Ĵ���һ����
A1 = upcoef2('a',cA1,'bior3.7',1);
H1 = upcoef2('h',cH1,'bior3.7',1);
V1 = upcoef2('v',cV1,'bior3.7',1);
D1 = upcoef2('d',cD1,'bior3.7',1);

figure('Name','DWT','NumberTitle','off');   % �½����ڣ�������ʾÿ��ͨ��
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
title('DWT ��任������');

