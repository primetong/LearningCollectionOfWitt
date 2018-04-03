% 使用MATLAB或第三方API，
% 用拉普拉斯算子和Kirsch方向算子实现边缘检测，
% 并利用某种跟踪法实现对边缘点的闭合操作。

% 读入图片
I = imread('rice.png');
if (size(I,3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);
figure('Name','原始图像','NumberTitle','off');
imshow(I);

%%%%%%%%%%%
% Laplace %
%%%%%%%%%%%
laplacian = [0 -1 0 ; -1 4 -1 ; 0 -1 0];
O = imfilter(I, laplacian);
F = O;

figure('Name','边缘检测','NumberTitle','off');
subplot(121);
imshow(O);
title('Laplace');

%%%%%%%%%%
% Kirsch %
%%%%%%%%%%
kirsch(:,:,1) = [3 3 3 ; 3 0 -5 ; 3 -5 -5];
kirsch(:,:,2) = [3 3 3 ; 3 0 3 ; -5 -5 -5];
kirsch(:,:,3) = [3 3 3 ; -5 0 3 ; -5 -5 3];
kirsch(:,:,4) = [-5 3 3 ; -5 0 3 ; -5 3 3];
kirsch(:,:,5) = [-5 -5 3 ; -5 0 3 ; 3 3 3];
kirsch(:,:,6) = [-5 -5 -5 ; 3 0 3 ; 3 3 3];
kirsch(:,:,7) = [3 -5 -5 ; 3 0 -5 ; 3 3 3];
kirsch(:,:,8) = [3 3 -5 ; 3 0 -5 ; 3 3 -5]; 

[m, n] = size(I);
K = zeros(m, n, 8);
for i = 1 : 8
    K(:,:,i) = imfilter(I, kirsch(:,:,i));
end

O = I;
for i = 1 : m
    for j = 1 : n
        O(i, j) = max([K(i,j,1),K(i,j,2),K(i,j,3),K(i,j,4),K(i,j,5),K(i,j,6),K(i,j,7),K(i,j,8)]);
    end;
end;

subplot(122);
imshow(O);
title('Kirsch');

%%%%%%%%%%
% 光栅法 %
%%%%%%%%%%
threshold_alpha = 70 / 255;
threshold_beta = 60 / 255;
O = zeros(m, n);

% 上到下
for i = 2 : m
    for j = 2 : n - 1
        if F(i, j) > threshold_beta
            O(i, j) = 1;
        end;
        if F(i, j) > threshold_alpha 
            if O(i - 1, j - 1) == 1 || O(i - 1, j) == 1 || O(i - 1, j + 1) == 1
                O(i, j) = 1;
            end;
        end;
    end;
end;

% 下到上
for i = 1 : m - 1
    for j = 2 : n - 1
        if F(i, j) > threshold_beta
            O(i, j) = 1;
        end;
        if F(i, j) > threshold_alpha 
            if O(i + 1, j - 1) == 1 || O(i + 1, j) == 1 || O(i + 1, j + 1) == 1
                O(i, j) = 1;
            end;
        end;
    end;
end;

% 左到右
for i = 2 : m - 1
    for j = 2 : n
        if F(i, j) > threshold_beta
            O(i, j) = 1;
        end;
        if F(i, j) > threshold_alpha 
            if O(i - 1, j - 1) == 1 || O(i, j - 1) == 1 || O(i + 1, j - 1) == 1
                O(i, j) = 1;
            end;
        end;
    end;
end;

% 右到左
for i = 2 : m - 1
    for j = 1 : n - 1
        if F(i, j) > threshold_beta
            O(i, j) = 1;
        end;
        if F(i, j) > threshold_alpha 
            if O(i - 1, j + 1) == 1 || O(i, j + 1) == 1 || O(i + 1, j + 1) == 1
                O(i, j) = 1;
            end;
        end;
    end;
end;

figure('Name','处理结果','NumberTitle','off');
imshow(O);
