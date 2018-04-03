% ʹ��MATLAB�������API��
% ��������˹���Ӻ�Kirsch��������ʵ�ֱ�Ե��⣬
% ������ĳ�ָ��ٷ�ʵ�ֶԱ�Ե��ıպϲ�����

% ����ͼƬ
I = imread('rice.png');
if (size(I,3) ~= 1)
    I = rgb2gray(I);
end
I = im2double(I);
figure('Name','ԭʼͼ��','NumberTitle','off');
imshow(I);

%%%%%%%%%%%
% Laplace %
%%%%%%%%%%%
laplacian = [0 -1 0 ; -1 4 -1 ; 0 -1 0];
O = imfilter(I, laplacian);
F = O;

figure('Name','��Ե���','NumberTitle','off');
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
% ��դ�� %
%%%%%%%%%%
threshold_alpha = 70 / 255;
threshold_beta = 60 / 255;
O = zeros(m, n);

% �ϵ���
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

% �µ���
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

% ����
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

% �ҵ���
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

figure('Name','������','NumberTitle','off');
imshow(O);
