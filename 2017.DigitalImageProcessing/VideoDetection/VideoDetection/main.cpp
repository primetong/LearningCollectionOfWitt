//���VS2013��fopen���Ϊfopen_s������
//����ͨ�Ľ������������ʹ��fopen_s���������fopen_s()�������÷�
//fopen_s(_Outptr_result_maybenull_ FILE ** _File, _In_z_ const char * _Filename, _In_z_ const char * _Mode);
//����fopen()������
//fopen(_In_z_ const char * _Filename, _In_z_ const char * _Mode);
//��fopen_s����Ҫ��fopen��һ�������ҷ��ص�����Ϊ��errno_t __cdecl����fopen()���ص�����Ϊ��FILE * __cdecl
//��ˣ�fopen_s�������ܲ����ʺ��Լ��ĳ��򣬽��������һ�ȽϺõķ�����
//����Ԥ�����壺
//��Ŀ->����->��������->C / C++->Ԥ������->Ԥ���������壬����_CRT_SECURE_NO_DEPRECATE
//�����Ϳ��Խ��vs2013����������ˡ�

//1�������˶�Ŀ����ķ����ܽ�
//��������ص����ף�Ŀǰ�ܹ�ʵ���˶�������ķ�����Ҫ�����¼��֣�
//1��������ַ������������ٵطָ���˶�ͼ���䲻��֮�������ܹ��߱仯Ӱ�죬�����ĸ����ǹؼ���������������ͷ�˶��������
//2�����������ܼ������˶���ͼ�񣬿���������ͷ�˶�����������Ǽ��㸴�Ӻ�ʱ������ʵ��ʵʱ��⣻
//3��֡����ܹ��߱仯Ӱ���С���򵥿��٣������ָܷ���������˶��������һ������Ŀ��ָ��㷨��
//   ����һЩ�Ľ����㷨����Ҫ�����ڼ��ٹ���Ӱ��ͼ����������仯�������Ǵ���������ж������ַ��������ۣ�
//   �����ǳ��νӴ���������Ŀ�������Ǿ�ֹ�������˲�����򵥵ķ�����������ַ���
//2��������ַ�ʵ�ֲ���
//�ɽ�������ַ���ʵ�ֲ����ܽ����£�
//1������ͼ���Ԥ������Ҫ������ͼ����лҶȻ��Լ��˲���
//   �ҶȻ��ķ�������C����ʵ�ֿɲο���Canny��Ե����㷨ԭ����VCʵ�����(��)��һ�ģ�
//   ����ͼ���˲���ͨ���ɲ��õķ�������ֵ�˲�����ֵ�˲��Լ���˹�˲��ȡ�
//   ���ڸ�˹�˲���ʵ���������˹ͼ���˲�ԭ��������ɢ��ʵ�ַ�����һ�ġ�
//2��������ģ�����Ǳ������Ϊ��Ҫ�ĵ�һ����
//   Ŀǰ����˼·���Ǹ���ǰN֡ͼ��ĻҶ�ֵ��������ͳ�ƴӶ��õ�һ������ͳ������ĳ�ʼ������
//   �ڵ�һ�ε�ʵ�ֹ����У����õ�һ��ͼƬ��Ϊ����ͼ�������Ƚϼ򵥡�
//3��ǰ����ȡ������ǰ���µ�ͼ���뱳�����������ñ�����ͼ��Ȼ�����һ���ķ����Ը�ͼ���ж�ֵ����
//   ���ջ���˶�ǰ�����򣬼�ʵ��ͼ��ָ

//���о����˶����ͱ������·���ʵ�ֵĲ������£�
//(1)���پ�̬�ڴ棬��ͼ����г�ʼ��׼���ɼ���
//(2)�ɼ�ͼ�񣬶������k����Ϊͼ�����м������ɼ���1��ͼ��ʱ������ݵ�һ֡�Ĵ�С��Ϣ���о���ͼ��ĳ�ʼ����
//���ҽ���һ֡ͼ����лҶȻ�������ת��Ϊ������Ϊ����ͼ�񼰾������k������1��ѵ�ǰ֡���лҶȻ�����
//��ת��Ϊ������Ϊ��ǰ֡��ͼ�񼰾����õ�ǰ֡��ͼ�����ͱ���֡��ͼ������������ǰ��ͼ���󲢶������
//��ֵ���Ա�������뱳��֡���ϴ�����ظ�����Ҳ���Ƕ�ֵ������ĸ�����
//����һ֡���������1W������������Ҫ����ǰ֡�洢Ϊ��һ֡�����ҽ�ϵͳ��״̬תΪ1�����ɼ��ڶ�֡��
//��һ֡�͵ڶ�֡�����ﶼ����1W��������ʱ������ǰ֡�洢Ϊ�ڶ�֡��ͨ���жϵ�һ֡�͵ڶ�֡�Ĳ�ֵ��ȷ����֡�Ƿ�������
//��������ϵͳ״̬תΪ2�����ɼ�����֡�����������򱨾�������ϵͳ״̬תΪ0�����ɼ�����֡��
//����һ֡�͵ڶ�֡�����ﶼ����1W��������, ������֡û��ʱ�򱨾���
//������3֡�����ﶼ����1W��������ʱ������ǰ֡�洢Ϊ����֡��ͨ���жϵڶ�֡�͵���֡�Ĳ�ֵ��ȷ����֡�Ƿ�������
//�������򽫸��±��������������򱨾���Ȼ���ϵͳ״̬תΪ0�����ɼ�����֡��
//ע��������һ��0��1��2��0....��״̬����
//cvCopy��ԭ���ǣ�
//void cvCopy(const CvArr* src, CvArr* dst, const CvArr* mask = NULL);
//��ʹ���������֮ǰ���������cvCreateImage����һ��ĺ����ȿ�һ���ڴ棬Ȼ�󴫵ݸ�dst��cvCopy���src�е����ݸ��Ƶ�dst���ڴ��С�
//
//cvCloneImage��ԭ���ǣ�
//IplImage* cvCloneImage(const IplImage* image);
//��ʹ�ú���֮ǰ�����ÿ����ڴ档�ú������Լ���һ���ڴ棬Ȼ���ƺ�image��������ݣ�Ȼ�������ڴ��е����ݷ��ظ��㡣
//clone�ǰ����еĶ����ƹ�����Ҳ����˵�������Ƿ�����Roi, Coi��Ӱ��copy�Ĳ�����clone����ԭ�ⲻ���Ŀ�¡������
//copy�Ͳ�һ����ֻ�Ḵ��ROI����ȡ�

#include <stdio.h>
#include <time.h>
#include <cv.h>
#include <cxcore.h>
#include <highgui.h>

////�������º������Զ��˶�Ŀ��Ѱ�����������ƾ��ο򣬵���Ч���������ҾͲ�������������
//CvMemStorage *stor;
//CvSeq *cont;
//void DrawRec(IplImage* pImgFrame, IplImage* pImgProcessed, int MaxArea);

//main(int argc, char *argv[])�е���������, argc��ʾ����������*argv���Ǿ���Ĳ���.
//Ĭ������£�project��������Ϊ��һ��������(���磬�ҵ�Ӧ�������test.exe����argv[0]��Ӧ��ֵΪtest.exe�ľ���·�� 
//- D:\program files\vs2012\vctest\debug\test.exe)����Ĭ�������argc��ֵΪ1(��ֵ�����ֶ��ı�), 
//������趨��������������ͨ����������:
//<1>ѡ��PROJECT��>Properties��>Configuration Properties��>Debugging��>Command Arguments
//<2>��Command Arguments����Ӳ��������� : Ҫ�趨argv[1] = ��23��, argv[2] = ��Hello��, 
//��ô����ֵ23  Hello����(����ֵ֮��ո����,Ҫ���ո�Ͱ�����" "��)Ȼ�󱣴漴�ɡ�����֮��ɷ��ֲ���ֵ�Ѿ��ı�
//���İ棺�˵�[��Ŀ]->����ҳ->��������->���ԣ���[�����в���]�����ϼ��ɡ���ͬ����֮ǰ�ÿո������

int main( int argc, char** argv )
{
//����IplImageָ��
  IplImage* pFrame = NULL;     //pFrameΪ��Ƶ��ȡ��һ֡
  IplImage* pFrame1 = NULL;      //��һ֡
  IplImage* pFrame2 = NULL;//�ڶ�֡
  IplImage* pFrame3 = NULL;//����֡
  IplImage* pFrImg = NULL;     //pFrImgΪ��ǰ֡�ĻҶ�ͼ
  IplImage* pBkImg = NULL;     //pBkImgΪ��ǰ�����ĻҶ�ͼ
  IplImage* pBkImgTran = NULL;//pBkImgTranΪ��ǰ�����������ͼ��
  IplImage* pFrImgTran = NULL;//pFrImgTranΪ��ǰǰ���������ͼ��
  CvMat* pFrameMat = NULL;     //pFrameMatΪ��ǰ�ҶȾ���
  CvMat* pFrMat = NULL;      //pFrMatΪ��ǰǰ��ͼ���󣬵�ǰ֡��ȥ����ͼ
  CvMat* bg1 = NULL;
  CvMat* bg2 = NULL;
  CvMat* bg3 = NULL;
  CvMat* pFrMatB = NULL;     //pFrMatBΪ��ֵ����0,1����ǰ��ͼ
  CvMat* pBkMat = NULL;
  CvMat* pZeroMat = NULL;               //���ڼ���bg1 - bg2 ��ֵ
  CvMat* pZeroMatB = NULL;//���ڼ��� pZeroMat��ֵ�������ж��ж��ٸ������ʱ����
  CvCapture* pCapture = NULL;
  int warningNum = 0;      //��⵽���������ֵĴ���
  int nFrmNum = 0;//֡����
  int status = 0;        //״̬��־λ
//��������
  cvNamedWindow("video", 1);
  cvNamedWindow("background",1);//����
  cvNamedWindow("foreground",1);//ǰ��
//ʹ������������
  cvMoveWindow("video", 30, 0);
  cvMoveWindow("background", 720, 0);
  cvMoveWindow("foreground", 365, 330);
  if ( argc > 2 )
    {
      fprintf(stderr, "Usage: bkgrd [video_file_name]\n");
      return -1;
    }
//������ͷ��������ͷȡ����������ʹ�ú��������Ƶ��������ģ������ͷ������cvCaptureFromCAM(0)��ֱ�Ӵ򿪱ʼǱ�����ͷ
  if (argc ==1)
    if ( !(pCapture = cvCaptureFromCAM(0)))
      {
        fprintf(stderr, "Can not open camera.\n");
        return -2;
      }
//����Ƶ�ļ�����ͨ��*argv�����
  if (argc == 2)
    if ( !(pCapture = cvCaptureFromFile(argv[1])))
      {
        fprintf(stderr, "Can not open video file %s\n", argv[1]);
        return -2;
      }

////��ʼ��ʱ
//  time_t start,end;
//  time(&start);        //time() ���ش�1970��1��1��00��00��00��ʼ���������ڵ���������10Ϊ���֣���
//  printf("%d\n",start);
//��֡��ȡ��Ƶ
  while (pFrame = cvQueryFrame( pCapture ))
    {
      nFrmNum++;
      //����ǵ�һ֡����Ҫ�����ڴ棬����ʼ��
      if (nFrmNum == 1)
        {
          pBkImg = cvCreateImage(cvSize(pFrame->width, pFrame->height), IPL_DEPTH_8U,1);
          pFrImg = cvCreateImage(cvSize(pFrame->width, pFrame->height), IPL_DEPTH_8U,1);
          pBkImgTran = cvCreateImage(cvSize(pFrame->width,pFrame->height), IPL_DEPTH_8U,1);
          pFrImgTran = cvCreateImage(cvSize(pFrame->width,pFrame->height), IPL_DEPTH_8U,1);
          pBkMat = cvCreateMat(pFrame->height, pFrame->width, CV_32FC1);
          pZeroMat = cvCreateMat(pFrame->height, pFrame->width, CV_32FC1);
          pFrMat = cvCreateMat(pFrame->height, pFrame->width, CV_32FC1);
          pFrMatB = cvCreateMat(pFrame->height, pFrame->width, CV_8UC1);
          pZeroMatB = cvCreateMat(pFrame->height, pFrame->width, CV_8UC1);
          pFrameMat = cvCreateMat(pFrame->height, pFrame->width, CV_32FC1);
          cvZero(pZeroMat);
          //ת���ɵ�ͨ��ͼ���ٴ���
          cvCvtColor(pFrame, pBkImg, CV_BGR2GRAY);
          //ת��Ϊ����
          cvConvert(pFrImg, pBkMat);
        }
      else /* ���ǵ�һ֡�ľ��������� */
        {
          //pFrImgΪ��ǰ֡�ĻҶ�ͼ
          cvCvtColor(pFrame, pFrImg, CV_BGR2GRAY);
          //pFrameMatΪ��ǰ�ҶȾ���
          cvConvert(pFrImg, pFrameMat);
          //pFrMatΪǰ��ͼ���󣬵�ǰ֡��ȥ����ͼ
          cvAbsDiff(pFrameMat, pBkMat, pFrMat);
          //pFrMatBΪ��ֵ����0,1����ǰ��ͼ
          cvThreshold(pFrMat,pFrMatB, 60, 1, CV_THRESH_BINARY);
          //��ͼ�����ת��Ϊͼ���ʽ��������ʾ
          cvConvert(pBkMat, pBkImgTran);   
          cvConvert(pFrMat, pFrImgTran);  
          //��ʾͼ��
          cvShowImage("video", pFrame);
          cvShowImage("background", pBkImgTran); //��ʾ����
          cvShowImage("foreground", pFrImgTran); //��ʾǰ��

		  //DrawRec(pFrame, pFrImgTran, 16);		//���˶�Ŀ��Ѱ�����������ƾ��ο򣬵���Ч���������Ҳ�������������

          //������ÿץȡһ֡��Ҫ���Ĺ������������Σ�ռ��
          if (cvCountNonZero(pFrMatB) > 10000 && status == 0) //��ʾ�ǵ�һ֡���������1W��������
            {/* ����Ҫ����ǰ֡�洢Ϊ��һ֡ */
              pFrame1 = cvCloneImage(pFrame);
              bg1 = cvCloneMat(pFrMat);
              status = 1;      //�����ɼ���2֡
            }
          else if (cvCountNonZero(pFrMatB) < 10000 && status == 1) // ��ʾ��һ֡���������1W�������㣬���ڶ�֡û��,�򱨾�
            {
              printf("NO.%d warning!!!!\n\n",warningNum++);
              status = 0;
            }
          else if (cvCountNonZero(pFrMatB) > 10000 && status == 1)// ��ʾ��һ֡�͵ڶ�֡�����ﶼ����1W��������
            {
              pFrame2 = cvCloneImage(pFrame);
              bg2 = cvCloneMat(pFrMat);
              cvAbsDiff(bg1, bg2, pZeroMat);
              cvThreshold(pZeroMat,pZeroMatB, 20, 1, CV_THRESH_BINARY);
              if (cvCountNonZero(pZeroMatB) > 3000 ) //��ʾ���ǲ������������Ļ�Ҫ����
                {
                  printf("NO.%d warning!!!!\n\n",warningNum++);
                  status = 0;
                }
              else
                {
                  status = 2;                   //�����ɼ���3֡
                }
            }
          else if (cvCountNonZero(pFrMatB) < 10000 && status == 2)//��ʾ��һ֡�͵ڶ�֡�����ﶼ����1W��������,������֡û��
            {
              //����
              printf("NO.%d warning!!!!\n\n",warningNum++);
              status = 0;
            }
          else if (cvCountNonZero(pFrMatB) > 10000 && status == 2)//��ʾ����3֡�����ﶼ����1W��������
            {
              pFrame3 = cvCloneImage(pFrame);
              bg3 = cvCloneMat(pFrMat);
              cvAbsDiff(bg2, bg3, pZeroMat);
              cvThreshold(pZeroMat,pZeroMatB, 20, 1, CV_THRESH_BINARY);
              if (cvCountNonZero(pZeroMatB) > 3000 ) //��ʾ���ǲ������������Ļ�Ҫ����
                {
                  printf("NO.%d warning!!!!\n\n",warningNum++);
                }
              else //��ʾbg2,bg3����
                {
                  cvReleaseMat(&pBkMat);
                  pBkMat = cvCloneMat(pFrameMat); //���±���
                }
                status = 0;                //������һ�βɼ�����
            }
          //����а����¼���������ѭ��
          //�˵ȴ�ҲΪcvShowImage�����ṩʱ�������ʾ
          //�ȴ�ʱ����Ը���CPU�ٶȵ���
          if ( cvWaitKey(2) >= 0 )
            break;
        }/* The End of the else */
    }/* The End of th while */
//���ٴ���
    cvDestroyWindow("video");
    cvDestroyWindow("background");
    cvDestroyWindow("foreground");
//�ͷ�ͼ��;���
    cvReleaseImage(&pFrImg);
    cvReleaseImage(&pBkImg);
    cvReleaseMat(&pFrameMat);
    cvReleaseMat(&pFrMat);
    cvReleaseMat(&pBkMat);
    cvReleaseCapture(&pCapture);
  return 0;
}

////�������º������Զ��˶�Ŀ��Ѱ�����������ƾ��ο򣬵���Ч���������ҾͲ�������������
//void DrawRec(IplImage* pImgFrame, IplImage* pImgProcessed, int MaxArea)
//{
//	//pImgFrame:��ʼδ�����֡���������������������;  
//	//pImgProcessed:�������֡,�������˶����������  
//
//	stor = cvCreateMemStorage(0);  //������̬�ṹ������  
//	cont = cvCreateSeq(CV_SEQ_ELTYPE_POINT, sizeof(CvSeq), sizeof(CvPoint), stor);
//
//	// �ҵ���������  
//	cvFindContours(pImgProcessed, stor, &cont, sizeof(CvContour),
//		CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
//
//	// ֱ��ʹ��CONTOUR�еľ�����������  
//	for (; cont; cont = cont->h_next)
//	{
//		CvRect r = ((CvContour*)cont)->rect;
//		if (r.height * r.width > MaxArea) // ���С�ķ���������  
//		{
//			cvRectangle(pImgFrame, cvPoint(r.x, r.y),
//				cvPoint(r.x + r.width, r.y + r.height),
//				CV_RGB(255, 0, 0), 1, CV_AA, 0);
//		}
//	}
//	cvShowImage("video-detection", pImgFrame);
//}