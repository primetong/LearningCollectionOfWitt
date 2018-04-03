% ����MATLAB���������ٿ���ƽ̨����дƵ���˲�����

% ��ȡ����
I = imread('cameraman.tif');
if (size(I, 3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);

figure('Name','Ƶ���˲�','NumberTitle','off');
subplot(221);
imshow(I);
title('ԭʼͼ��');

F = fft2(I);        % �ԻҶ�ͼ����� FFT
F = fftshift(F);    % ������Ҷ�仯��ͼ���Ƶ�ͼ�����ĶԳƵ�λ��

%%%%%%%%%%%%%%%%%%%%%%%
% Butterworth ��ͨ�˲� %
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

%��Ƶ��ת���ؿ���
G = real(ifft2(ifftshift(G)));
subplot(223)
imshow(G);
title('Butterworth ��ͨ�˲�');

%%%%%%%%%%%%%%%
% ��ͨά���˲� %
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
 
%��Ƶ��ת���ؿ���
O = real(ifft2(ifftshift(O)));

subplot(224);
imshow(O);
title('��ͨά���˲�');
