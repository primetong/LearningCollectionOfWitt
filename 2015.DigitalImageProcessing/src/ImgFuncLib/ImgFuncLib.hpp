/*********************************************************************
 *	@file		ImgFuncLib.hpp
 *	@brief		
 *********************************************************************/
 
#pragma once

/*
 * 参考资料：
 * 
 * [Walkthrough: Creating and Using a Dynamic Link Library (C++)](https://msdn.microsoft.com/en-us/library/vstudio/ms235636(v=vs.140).aspx)
 * > By default, the New Project template for a DLL adds PROJECTNAME_EXPORTS to the defined symbols for the DLL project
 *
 * [在 WPF 中，使用 C++ 编写的 DLL 文件](http://blog.csdn.net/jarvischu/article/details/6634185)
 */

#ifdef IMGFUNCLIB_EXPORTS
#define IMGFUNCLIB_API __declspec(dllexport) 
#else
#define IMGFUNCLIB_API __declspec(dllimport) 
#endif


/*
 * \brief	imgf 是 ImgFuncsDll 的主要命名空间，
 *			包含处理 bgra32 图片的各个方法
 */
namespace imgf {

	typedef unsigned int uint;
	typedef unsigned char byte;
	
	/*
	 * \brief	标记颜色的类型
	 *			由于使用的是 bgra32 图像(B8 G8 R8 A8)，所以定义的顺序是这样
	 */
	enum ColorType : imgf::uint
	{
		Begin = 0u,
		B = 0u,
		G = 1u,
		R = 2u,
		A = 3u,
		End = 4u,
	};

	extern "C" IMGFUNCLIB_API bool is_loaded();

	/*
	 *	\brief	对图像进行“采样”操作
	 *	\param	bgra32src		一个 bgra32 的 bmp 图像的数组
	 *	\param	dst				输出的目标数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	rate			采样的倍率，倍率越高，越清晰
	 */
	extern "C" IMGFUNCLIB_API void sample(const byte bgra32src[], byte dst[], uint wid, uint hgt, uint rate);
	
	/*
	 *	\brief	对图像进行“量化”操作
	 *	\param	bgra32src		一个 bgra32 的 bmp 图像的数组
	 *	\param	dst				输出的目标数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	lv				量化级别，量化级别越低，颜色越少
	 */
	extern "C" IMGFUNCLIB_API void quantize(const byte bgra32src[], byte dst[], uint wid, uint hgt, uint lv);

	/*
	 *	\brief	对 8bit 灰度图像进行“掩码操作”操作
	 *	\param	gray8src		一个 gray8src 的 bmp 图像的数组
	 *	\param	dst				输出的目标数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	mask			掩码规则
	 */
	extern "C" IMGFUNCLIB_API void mask(const byte gray8src[], byte dst[], uint wid, uint hgt, uint bit);

	/*
	 *	\brief	对 8bit 灰度图像进行取二值化操作，min ~ max 之间的像素会变为白色
	 *	\param	gray8src		一个 gray8src 的 bmp 图像的数组
	 *	\param	dst				输出的目标数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	min				区间最小值
	 *	\param	max				区间最大值
	 */
	extern "C" IMGFUNCLIB_API void threshold(const byte gray8src[], byte dst[], uint wid, uint hgt, uint min, uint max);

	/*
	 *	\brief	对 8bit 灰度图像 计算 灰度直方图，并获取均值、方差等数据
	 *	\param	gray8src		一个 gray8src 的 bmp 图像的数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	cnt				256 长度的数组，记录每种灰度的像素的数量
	 *	\param	data			返回值数组，分别：{灰度均值，灰度中值，灰度方差，像素总数}
	 */
	extern "C" IMGFUNCLIB_API void histogram(const byte gray8src[], uint wid, uint hgt, uint cnt[256u], uint data[4u]);

	/*
	 *	\brief	对 8bit 灰度图像 计算 出其字符串画，（会变更 length 大小为字符串大小）
	 *	\param	gray8src		一个 gray8src 的 bmp 图像的数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	buffer			记录产生的 字符画
	 *	\param	length			字符画 的实际长度
	 */
	extern "C" IMGFUNCLIB_API void img2txt(const byte gray8src[], uint wid, uint hgt, byte buffer[], uint & length);

	/*
	 *	\brief	对 8bit 灰度图像 进行 伽马变换
	 *			[wiki: Gamma correction](https://en.wikipedia.org/wiki/Gamma_correction)
	 *	\param	gray8src		一个 gray8src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	r				Gamma 值
	 *	\param	eps				补偿系数
	 */
	extern "C" IMGFUNCLIB_API void gamma_correction(const byte gray8src[], byte dst[], uint wid, uint hgt, float r, float eps);

	/*
	 *	\brief	对 8bit 灰度图像 进行 对数变换（f(x) = c * log(1 + v * x) / log(v + 1)）
	 *	\param	gray8src		一个 gray8src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	c				系数 c
	 *	\param	v				系数 v
	 */
	extern "C" IMGFUNCLIB_API void logarithmic_transformation(const byte gray8src[], byte dst[], uint wid, uint hgt, float c, float v);

	/*
	 *	\brief	对 8bit 灰度图像 进行 灰度拉伸
	 *			[Contrast stretching](http://academic.mu.edu/phys/matthysd/web226/Lab01.htm)
	 *	\param	gray8src		一个 gray8src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	a				系数 a, 最小值
	 *	\param	b				系数 b, 最大值
	 */
	extern "C" IMGFUNCLIB_API void contrast_stretching(const byte gray8src[], byte dst[], uint wid, uint hgt, float a, float b);

	/*
	 *	\brief	对 bgra32 图像 进行 直方图均衡
	 *			[Histogram equalization](https://en.wikipedia.org/wiki/Histogram_equalization)
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 */
	extern "C" IMGFUNCLIB_API void histogram_equalization(const byte bgra32src[], byte dst[], uint wid, uint hgt);

	/*
	 *	\brief	对 bgra32 图像进行平移，旋转，缩放（使用邻近插值）
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	deltaX			位移量 X
	 *	\param	deltaY			位移量 Y
	 *	\param	angle			旋转角度
	 *	\param	scale			缩放比例
	 */
	extern "C" IMGFUNCLIB_API void neighbor_transform(const byte bgra32src[], byte dst[], uint wid, uint hgt, float deltaX, float deltaY, float angle, float scale);

	/*
	 *	\brief	对 bgra32 图像进行平移，旋转，缩放（使用双线性）
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	deltaX			位移量 X
	 *	\param	deltaY			位移量 Y
	 *	\param	angle			旋转角度
	 *	\param	scale			缩放比例
	 */
	extern "C" IMGFUNCLIB_API void bilinear_transform(const byte bgra32src[], byte dst[], uint wid, uint hgt, float deltaX, float deltaY, float angle, float scale);

	/*
	 *	\brief	对 bgra32 图像进行滤波操作
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	matrix			滤波模板
	 *	\param	matrix_width	模板的宽度
	 *	\param	matrix_height	模板的高度
	 */
	extern "C" IMGFUNCLIB_API void filter(const byte bgra32src[], byte dst[], uint wid, uint hgt, float matrix[], uint matrix_width, uint matrix_height);

	/*
	 *	\brief	对 bgra32 图像进行滤波操作，针对整数模板的版本
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	matrix			滤波模板（限整数）
	 *	\param	matrix_width	模板的宽度
	 *	\param	matrix_height	模板的高度
	 */
	extern "C" IMGFUNCLIB_API void filter_i(const byte bgra32src[], byte dst[], uint wid, uint hgt, int matrix[], uint matrix_width, uint matrix_height);

	/*
	 *	\brief	对 bgra32 图像进行 roberts 滤波操作
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 */
	extern "C" IMGFUNCLIB_API void filter_roberts(const byte bgra32src[], byte dst[], uint wid, uint hgt);

	/*
	 *	\brief	对 bgra32 图像进行 sobel 滤波操作
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 */
	extern "C" IMGFUNCLIB_API void filter_sobel(const byte bgra32src[], byte dst[], uint wid, uint hgt);

	/*
	 *	\brief	对 bgra32 图像进行 中值 滤波操作
	 *	\param	bgra32src		一个 bgra32src 的 bmp 图像的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 */
	extern "C" IMGFUNCLIB_API void filter_median(const byte bgra32src[], byte dst[], uint wid, uint hgt);

	/*
	 *	\brief	对 灰度 图像进行 膨胀操作
	 *	\param	gray8src		一个灰度图（二值图）的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 */
	extern "C" IMGFUNCLIB_API void morphology_expand(const byte gray8src[], byte dst[], uint wid, uint hgt);

	/*
	 *	\brief	对 灰度 图像进行 膨胀操作
	 *	\param	gray8src		一个灰度图（二值图）的数组
	 *	\param	dest			处理结果
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 */
	extern "C" IMGFUNCLIB_API void morphology_corrode(const byte gray8src[], byte dst[], uint wid, uint hgt);

	/*
	 *	\brief	RGBA 图像转化为 8bit 灰度图
	 *	\param	bgra32src		一个 32bit 的 bgra 彩色图数组
	 *	\param	gray8src		处理结果，一个 8bit 的灰度图数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 */
	extern "C" IMGFUNCLIB_API void bgra32_to_gray8(const byte bgra32src[], byte gray8src[], uint wid, uint hgt);

	/*
	 *	\brief	哈夫曼编码
	 *	\param	src				待处理的字节数组
	 *	\param	src_size		图像宽度
	 *	\param	dst				处理结果的长度
	 *	\param	dst_size		图像高度
	 *	\return					处理结果的实际长度，如果 < 0，则发生了错误
	 */
	extern "C" IMGFUNCLIB_API int  huffman_encode(const byte src[], uint src_size, byte dst[], uint dst_size);
	
	/*
	 *	\brief	解哈夫曼编码
	 *	\param	src				待处理的字节数组
	 *	\param	src_size		图像宽度
	 *	\param	dst				处理结果的长度
	 *	\param	dst_size		图像高度
	 *	\return					处理结果的实际长度，如果 < 0，则发生了错误
	 */
	extern "C" IMGFUNCLIB_API int  huffman_decode(const byte src[], uint src_size, byte dst[], uint dst_size);

	/*
	 *	\brief	从一个 byte[] 中提取某个像素的某个颜色值
	 *	\param	bgra32src		一个 bgra32 的 bmp 图像的数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	x				目标点的 x 坐标
	 *	\param	y				目标点的 y 坐标
	 *	\param	type			颜色的类型
	 *	\return					点的颜色值，范围为 0~255u
	 */
	inline byte & bgra32color(byte bgra32src[], uint wid, uint hgt, uint x, uint y, ColorType type);
	
	/*
	 *	\brief	从一个 byte[] 中提取某个像素的某个颜色值，针对 const 的重载
	 *	\param	bgra32src		一个 bgra32 的 bmp 图像的数组
	 *	\param	wid				图像宽度
	 *	\param	hgt				图像高度
	 *	\param	x				目标点的 x 坐标
	 *	\param	y				目标点的 y 坐标
	 *	\param	type			颜色的类型
	 *	\return					点的颜色值，范围为 0~255u
	 */
	inline byte bgra32color(const byte bgra32src[], uint wid, uint hgt, uint x, uint y, ColorType type);

}




