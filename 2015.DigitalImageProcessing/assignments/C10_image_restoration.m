% ʹ��MATLAB���ͼ��ԭ�ĺ�����
% �������ͼ���дͼ��ԭ�ĳ���
% �ص�۲첻ͬ������ͼ��ԭ��Ӱ�졣

I = imread('image_restoration.png');

G = fspecial('motion', 10, 160);     % ���˶�ģ�����ӣ��˶������ص���룬�Ƕȣ�
F = imfilter(I, G);

figure('Name', '������', 'NumberTitle', 'off');
imshow(F);
