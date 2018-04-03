/*********************************************************************
 *	@file		ImgFuncsDll.cpp
 *	@brief		ImgFuncsDll 中，函数的具体实现
 *********************************************************************/

#include <cmath>

/*
 * 解决方法受到
 * [Visual Studio 2013 doesn't ignore disabled warnings](http://stackoverflow.com/questions/24244544/visual-studio-2013-doesnt-ignore-disabled-warnings)
 * 启发
 */

#ifndef D_SCL_SECURE_NO_WARNINGS
#pragma warning(push)
#pragma warning(disable: 4996)
#endif

#include <algorithm>

#ifndef D_SCL_SECURE_NO_WARNINGS
#pragma warning(pop)
#endif

#include <array>
#include <vector>
#include <queue>
#include <utility>

#include "ImgFuncLib.hpp"



IMGFUNCLIB_API bool imgf::
is_loaded() 
{
	return true;
}


/* 参考资料：[图像的采样与量化及灰度直方图](http://blog.csdn.net/luoweifu/article/details/8043967) */
IMGFUNCLIB_API void imgf::
sample(const byte bgra32src[], byte dst[], uint wid, uint hgt, uint rate)
{
	/* src 中 颜色排列顺序是 B8 G8 R8 A8 */
	
	/* 采样倍率上限 */
	static const uint rate_max = std::max(wid, hgt);
	/* 采样间隔 */
	auto delta = std::max(rate_max / rate, 1u);

	auto sumWid = wid / delta + 1u;
	auto sumHgt = hgt / delta + 1u;
	auto capacity = sumWid * sumHgt;
	auto sum = new uint[capacity];
	auto cnt = new uint[capacity];

	/* 开始采样处理 */
	for (auto type = ColorType::Begin; 
		type < ColorType::End;
		type = static_cast<ColorType>(type + 1)) 
	{
		std::fill(sum, sum + capacity, 0u);
		std::fill(cnt, cnt + capacity, 0u);
		
		/* 统计颜色信息 */
		for (auto x = 0u; x < wid; x++) {
			for (auto y = 0u; y < hgt; y++) {
				auto srcX = x / delta;
				auto srcY = y / delta;
				auto i = srcX + srcY * sumWid;
				sum[i] += bgra32color(bgra32src, wid, hgt, x, y, type);
				cnt[i]++;
			}
		}

		/* 取均值 */
		for (auto i = 0u; i < capacity; i++)
			if (cnt[i])
				sum[i] /= cnt[i];
			else
				sum[i] = 0;

		/* 赋值到对象 */
		for (auto x = 0u; x < wid; x++) {
			for (auto y = 0u; y < hgt; y++) {
				auto srcX = x / delta;
				auto srcY = y / delta;
				bgra32color(dst, wid, hgt, x, y, type) = sum[srcX + srcY * sumWid];
			}
		}
	}
	
	delete sum;
	delete cnt;
}

/* 参考资料，同上 */
IMGFUNCLIB_API void imgf::
quantize(const byte bgra32src[], byte dst[], uint wid, uint hgt, uint lv)
{
	/* src 中 颜色排列顺序是 B8 G8 R8 A8 */

	/* 单个通道，颜色的最多数量 */
	static const uint color_max = 256u;
	/* 查表的方式，速度更快。这个表是某个程序生成的，见本文件下方的附录 */
	static const byte quantize[8u][color_max] = 
	{
		{   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255},
		{   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  85,  85
		,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85
		,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85
		,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85
		,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85
		,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85
		,  85,  85,  85,  85,  85,  85,  85,  85, 170, 170, 170, 170, 170, 170, 170
		, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170
		, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170
		, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170
		, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170
		, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170
		, 170, 170, 170, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255
		, 255},
		{   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0
		,   0,   0,   0,   0,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36
		,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36
		,  36,  36,  36,  36,  36,  36,  36,  36,  36,  36,  72,  72,  72,  72,  72
		,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72
		,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72,  72
		,  72,  72, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109
		, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109
		, 109, 109, 109, 109, 109, 109, 109, 109, 145, 145, 145, 145, 145, 145, 145
		, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145
		, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 145, 182
		, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182
		, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182, 182
		, 182, 182, 182, 182, 182, 182, 218, 218, 218, 218, 218, 218, 218, 218, 218
		, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218
		, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 218, 254, 254, 254
		, 254, 254, 254, 254, 254, 254, 254, 254, 254, 254, 254, 254, 254, 254, 254
		, 254},
		{   0,   0,   0,   0,   0,   0,   0,   0,   0,  17,  17,  17,  17,  17,  17
		,  17,  17,  17,  17,  17,  17,  17,  17,  17,  17,  17,  34,  34,  34,  34
		,  34,  34,  34,  34,  34,  34,  34,  34,  34,  34,  34,  34,  34,  51,  51
		,  51,  51,  51,  51,  51,  51,  51,  51,  51,  51,  51,  51,  51,  51,  51
		,  68,  68,  68,  68,  68,  68,  68,  68,  68,  68,  68,  68,  68,  68,  68
		,  68,  68,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85,  85
		,  85,  85,  85,  85, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102
		, 102, 102, 102, 102, 102, 102, 119, 119, 119, 119, 119, 119, 119, 119, 119
		, 119, 119, 119, 119, 119, 119, 119, 119, 136, 136, 136, 136, 136, 136, 136
		, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 153, 153, 153, 153, 153
		, 153, 153, 153, 153, 153, 153, 153, 153, 153, 153, 153, 153, 170, 170, 170
		, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 187
		, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187
		, 187, 204, 204, 204, 204, 204, 204, 204, 204, 204, 204, 204, 204, 204, 204
		, 204, 204, 204, 221, 221, 221, 221, 221, 221, 221, 221, 221, 221, 221, 221
		, 221, 221, 221, 221, 221, 238, 238, 238, 238, 238, 238, 238, 238, 238, 238
		, 238, 238, 238, 238, 238, 238, 238, 255, 255, 255, 255, 255, 255, 255, 255
		, 255},
		{   0,   0,   0,   0,   0,   8,   8,   8,   8,   8,   8,   8,   8,  16,  16
		,  16,  16,  16,  16,  16,  16,  24,  24,  24,  24,  24,  24,  24,  24,  32
		,  32,  32,  32,  32,  32,  32,  32,  32,  41,  41,  41,  41,  41,  41,  41
		,  41,  49,  49,  49,  49,  49,  49,  49,  49,  57,  57,  57,  57,  57,  57
		,  57,  57,  65,  65,  65,  65,  65,  65,  65,  65,  74,  74,  74,  74,  74
		,  74,  74,  74,  74,  82,  82,  82,  82,  82,  82,  82,  82,  90,  90,  90
		,  90,  90,  90,  90,  90,  98,  98,  98,  98,  98,  98,  98,  98, 106, 106
		, 106, 106, 106, 106, 106, 106, 106, 115, 115, 115, 115, 115, 115, 115, 115
		, 123, 123, 123, 123, 123, 123, 123, 123, 131, 131, 131, 131, 131, 131, 131
		, 131, 139, 139, 139, 139, 139, 139, 139, 139, 148, 148, 148, 148, 148, 148
		, 148, 148, 148, 156, 156, 156, 156, 156, 156, 156, 156, 164, 164, 164, 164
		, 164, 164, 164, 164, 172, 172, 172, 172, 172, 172, 172, 172, 180, 180, 180
		, 180, 180, 180, 180, 180, 180, 189, 189, 189, 189, 189, 189, 189, 189, 197
		, 197, 197, 197, 197, 197, 197, 197, 205, 205, 205, 205, 205, 205, 205, 205
		, 213, 213, 213, 213, 213, 213, 213, 213, 222, 222, 222, 222, 222, 222, 222
		, 222, 222, 230, 230, 230, 230, 230, 230, 230, 230, 238, 238, 238, 238, 238
		, 238, 238, 238, 246, 246, 246, 246, 246, 246, 246, 246, 255, 255, 255, 255
		, 255},
		{   0,   0,   0,   4,   4,   4,   4,   8,   8,   8,   8,  12,  12,  12,  12
		,  16,  16,  16,  16,  20,  20,  20,  20,  24,  24,  24,  24,  28,  28,  28
		,  28,  32,  32,  32,  32,  36,  36,  36,  36,  40,  40,  40,  40,  44,  44
		,  44,  44,  48,  48,  48,  48,  52,  52,  52,  52,  56,  56,  56,  56,  60
		,  60,  60,  60,  64,  64,  64,  64,  68,  68,  68,  68,  72,  72,  72,  72
		,  76,  76,  76,  76,  80,  80,  80,  80,  85,  85,  85,  85,  85,  89,  89
		,  89,  89,  93,  93,  93,  93,  97,  97,  97,  97, 101, 101, 101, 101, 105
		, 105, 105, 105, 109, 109, 109, 109, 113, 113, 113, 113, 117, 117, 117, 117
		, 121, 121, 121, 121, 125, 125, 125, 125, 129, 129, 129, 129, 133, 133, 133
		, 133, 137, 137, 137, 137, 141, 141, 141, 141, 145, 145, 145, 145, 149, 149
		, 149, 149, 153, 153, 153, 153, 157, 157, 157, 157, 161, 161, 161, 161, 165
		, 165, 165, 165, 170, 170, 170, 170, 170, 174, 174, 174, 174, 178, 178, 178
		, 178, 182, 182, 182, 182, 186, 186, 186, 186, 190, 190, 190, 190, 194, 194
		, 194, 194, 198, 198, 198, 198, 202, 202, 202, 202, 206, 206, 206, 206, 210
		, 210, 210, 210, 214, 214, 214, 214, 218, 218, 218, 218, 222, 222, 222, 222
		, 226, 226, 226, 226, 230, 230, 230, 230, 234, 234, 234, 234, 238, 238, 238
		, 238, 242, 242, 242, 242, 246, 246, 246, 246, 250, 250, 250, 250, 254, 254
		, 254},
		{   0,   0,   2,   2,   4,   4,   6,   6,   8,   8,  10,  10,  12,  12,  14
		,  14,  16,  16,  18,  18,  20,  20,  22,  22,  24,  24,  26,  26,  28,  28
		,  30,  30,  32,  32,  34,  34,  36,  36,  38,  38,  40,  40,  42,  42,  44
		,  44,  46,  46,  48,  48,  50,  50,  52,  52,  54,  54,  56,  56,  58,  58
		,  60,  60,  62,  62,  64,  64,  66,  66,  68,  68,  70,  70,  72,  72,  74
		,  74,  76,  76,  78,  78,  80,  80,  82,  82,  84,  84,  86,  86,  88,  88
		,  90,  90,  92,  92,  94,  94,  96,  96,  98,  98, 100, 100, 102, 102, 104
		, 104, 106, 106, 108, 108, 110, 110, 112, 112, 114, 114, 116, 116, 118, 118
		, 120, 120, 122, 122, 124, 124, 126, 126, 128, 128, 130, 130, 132, 132, 134
		, 134, 136, 136, 138, 138, 140, 140, 142, 142, 144, 144, 146, 146, 148, 148
		, 150, 150, 152, 152, 154, 154, 156, 156, 158, 158, 160, 160, 162, 162, 164
		, 164, 166, 166, 168, 168, 170, 170, 172, 172, 174, 174, 176, 176, 178, 178
		, 180, 180, 182, 182, 184, 184, 186, 186, 188, 188, 190, 190, 192, 192, 194
		, 194, 196, 196, 198, 198, 200, 200, 202, 202, 204, 204, 206, 206, 208, 208
		, 210, 210, 212, 212, 214, 214, 216, 216, 218, 218, 220, 220, 222, 222, 224
		, 224, 226, 226, 228, 228, 230, 230, 232, 232, 234, 234, 236, 236, 238, 238
		, 240, 240, 242, 242, 244, 244, 246, 246, 248, 248, 250, 250, 252, 252, 255
		, 255},
		{   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14
		,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29
		,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44
		,  45,  46,  47,  48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59
		,  60,  61,  62,  63,  64,  65,  66,  67,  68,  69,  70,  71,  72,  73,  74
		,  75,  76,  77,  78,  79,  80,  81,  82,  83,  84,  85,  86,  87,  88,  89
		,  90,  91,  92,  93,  94,  95,  96,  97,  98,  99, 100, 101, 102, 103, 104
		, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119
		, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134
		, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149
		, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164
		, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179
		, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194
		, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209
		, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224
		, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239
		, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254
		, 255}
	};

	/* 确定 quantize_idx 的大小，例如 f(lv), f(1)=0, f(2~3)=1, f(4^7)=2, ... */
	lv = std::max(std::min(lv, color_max), 2U);
	auto quantize_idx 
		= (lv > 0u) 
		? static_cast<uint>(std::log2(lv)) - 1U
		: 0u
		;
	/* 取得对应的表，使得 dst[i] = f(src[i]) */
	auto & f = quantize[quantize_idx];

	/* 开始量化处理 */
	auto total = (wid * hgt << 2u);
	for (auto i = 0u; i < total; i++)
		dst[i] = f[bgra32src[i]];
}


IMGFUNCLIB_API void imgf::
mask(const byte gray8src[], byte dst[], uint wid, uint hgt, uint bit)
{
	auto src_end = wid * hgt;
	if (--bit < sizeof(byte) * 8u) {
		byte byte_mask = 1u << bit;
		for (auto i = 0u; i < src_end; i++)
			dst[i] = (gray8src[i] & byte_mask) ? (byte)~0u : 0u;
	} else {
		std::copy(gray8src, gray8src + src_end, dst);
	}
}


IMGFUNCLIB_API void imgf::
threshold(const byte gray8src[], byte dst[], uint wid, uint hgt, uint min, uint max)
{
	auto src_end = wid * hgt;
	for (auto i = 0u; i < src_end; i++)
		dst[i] = (min <= gray8src[i] && gray8src[i] <= max) ? (byte)~0u : 0u;
}

IMGFUNCLIB_API void imgf::
histogram(const byte gray8src[], uint wid, uint hgt, uint cnt[256u], uint data[4u])
{
	/* 初始化 */

	static const uint VALUE_MAX = 256u;
	for (auto i = 0u; i < VALUE_MAX; i++)
		cnt[i] = 0;

	/* 计算像素总数 */

	uint total = wid * hgt;
	
	/* 计算平均数 */

	uint sum = 0u;
	for (auto i = 0u; i < total; i++) {
		auto value = gray8src[i];
		cnt[value]++;
		sum += value;
	}
	uint mean = sum / total;
	
	/* 计算中位数 */
	
	uint mid = 0u;
	for (auto i(0u), cur(0u); i < VALUE_MAX; i++) {
		cur += cnt[i];
		if ((cur << 1u) >= total) {
			mid = i;
			break;
		}
	}

	/* 计算方差 */
	
	uint standard_deviation = 0u;
	for (auto i = 0u; i < total; i++) {
		auto value = gray8src[i];
		auto delta = value > mean ? value - mean : mean - value;
		standard_deviation += delta * delta;
	}
	standard_deviation /= VALUE_MAX;
	standard_deviation = (uint)sqrt(standard_deviation);

	/* 保存结果 */
	data[0] = mean;
	data[1] = mid;
	data[2] = standard_deviation;
	data[3] = total;
}

IMGFUNCLIB_API void imgf::
img2txt(const byte gray8src[], uint wid, uint hgt, byte buffer[], uint & length)
{
	/* 强制转换为偶数行 */
	hgt = hgt >> 1U << 1U;
	
	/* 检查 buffer 长度是否够 */
	uint min_length = (hgt >> 1U) * (wid + 2U);
	if (length < min_length) {
		length = 0U;
		return;
	}

	/* 预设常数 */

	static const uint interval = 64U;
	static const uint level = 256U / interval;
	static const byte f[level][level] = 
	{
		{ 'M',  'A',  'L', 'u'},
		{ 'V',  'B',  'l', ','},
		{ 'T',  '7',  'o', '.'},
		{ '^', '\"', '\'', ' '}
	};

	/* 开始转换 */

	auto idx = 0U;
	
	for (auto y = 0u; y < hgt; y++, y++) {
		auto line_t = y * wid;
		auto line_b = (y + 1) * wid;
		
		for (auto x = 0u; x < wid; x++) 
			buffer[idx++] = f[gray8src[x + line_t] / interval][gray8src[x + line_b] / interval];
	
		buffer[idx++] = '\r';
		buffer[idx++] = '\n';
	}

	/* 修正 length 为实际大小 */

	length = idx - 1;
}

IMGFUNCLIB_API void imgf::
gamma_correction(const byte gray8src[], byte dst[], uint wid, uint hgt, float r, float eps)
{
	static const byte out_max = 255U;
	static const float out_maxf = static_cast<float>(out_max);

	uint n = wid * hgt;
	for (uint i = 0U; i < n; i++) {
		float v_out = std::powf(static_cast<float>(gray8src[i]) / out_maxf + eps, r);
		dst[i] 
			= v_out > 1.0f
			? out_max
			: v_out < 0.f
			? 0U
			: static_cast<byte>(v_out * out_maxf)
			;
	}
}

IMGFUNCLIB_API void imgf::
logarithmic_transformation(const byte gray8src[], byte dst[], uint wid, uint hgt, float c, float v)
{
	static const byte out_max = 255U;
	static const float out_maxf = static_cast<float>(out_max);

	float base = std::logf(v + 1.0f);
	uint n = wid * hgt;
	v /= out_maxf;
	for (uint i = 0U; i < n; i++) {
		float out = c * std::logf(1.0f + v * static_cast<float>(gray8src[i])) / base;
		dst[i] 
			= out > 1.0f
			? out_max
			: out < 0.f
			? 0U
			: static_cast<byte>(out * out_maxf)
			;
	}
}

IMGFUNCLIB_API void imgf::
contrast_stretching(const byte gray8src[], byte dst[], uint wid, uint hgt, float a, float b)
{
	uint n = wid * hgt;
	byte max(0), min(255u);

	for (uint i = 0U; i < n; i++) {
		if(max < gray8src[i])
			max = gray8src[i];

		if (min > gray8src[i])
			min = gray8src[i];
	}

	if (min >= max)
		min = max - 1;

	uint k_numerator(static_cast<uint>((b - a) * 255.0f)), k_denominator(max - min);

	for (uint i = 0U; i < n; i++) {
		uint out = (gray8src[i] - min) * k_numerator / k_denominator + static_cast<uint>(a);
		dst[i] = std::min(255u, out);
	}
}

IMGFUNCLIB_API void imgf::
histogram_equalization(const byte bgra32src[], byte dst[], uint wid, uint hgt)
{
	static const uint LEVEL = 256u;
	
	/* 拷贝一份，用于复制透明通道 */
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);
	auto total = wid * hgt;
	for (auto i = 0U; i < total; i++)
		ret[i] = src[i];

	/* 开始对 G B R 执行直方图均衡 */

	auto cdf = new uint[LEVEL];
	auto h = new byte[LEVEL];
	
	auto type = 0U;
	while (type + 1U < ColorType::End) 
	{
		/* 统计单通道的像素分布 */
		memset(cdf, 0, LEVEL * sizeof(uint));
		for (auto i = 0u; i < total; i++)
			cdf[bgra32src[(i << 2u) | type]]++;

		/* 求取 cdf（见 wiki 上的资料） */
		for (auto i = 1u; i < LEVEL; i++)
			cdf[i] += cdf[i - 1U];
		
		/* 计算映射表 h */
		uint k_numerator(LEVEL - 1u), k_denominator(total - cdf[0]);
		memset(h, 0, LEVEL * sizeof(byte));
		for (auto i = 0u; i < LEVEL; i++)
			if (cdf[i])
				h[i] = (cdf[i] - cdf[0]) * k_numerator / k_denominator;
		
		/* 保存结果到 dst[] */
		for (auto i = 0u; i < total; i++) {
			uint idx = (i << 2u) | type;
			dst[idx] = h[bgra32src[idx]];
		}
		
		type++;
	}

	delete h;
	delete cdf;
}

IMGFUNCLIB_API void imgf::
neighbor_transform(const byte bgra32src[], byte dst[], uint wid, uint hgt, float deltaX, float deltaY, float angle, float scale)
{
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);

	int w = static_cast<int>(wid);
	int h = static_cast<int>(hgt);
	int mid_x = w >> 1;
	int mid_y = h >> 1;
	float mid_xf = static_cast<float>(wid) * 0.5f;
	float mid_yf = static_cast<float>(hgt) * 0.5f;
	float sina = std::sinf(angle);
	float cosa = std::cosf(angle);
	float scaleT = 1.0f / scale;

	/*
	 * 推的式子
	 * 参考：[图像旋转](http://blog.csdn.net/leaf6094189/article/details/8778808)
	 *
	 * src(x,y) => dst(xr,yr)
	 * 
	 * x0 = (x - mid_x) * scale
	 * y0 = (mid_y - y) * scale
	 * x1 = x0*cosa + y0*sina
	 * y1 = y0*cosa - x0*sina
	 * x2 = x1 + deltaX
	 * y2 = y1 + deltaY
	 * xr = x2 + mid_x
	 * yr = mid_y - y2 
	 * 
	 * dst(xr,yr) => src(x,y)
	 * 
	 * x2 = xr - mid_x
	 * y2 = mid_y - yr
	 * x1 = x2 - deltaX
	 * y1 = y2 - deltaY
	 * x0 = x1 * cosa - y1 * sina
	 * y0 = x1 * sina + y1 * cosa
	 * x = x0 / scale + mid_x
	 * y = mid_y - y0 / scale
	 */

	for (int x = 0; x < w; x++) {
		for (int y = 0; y < h; y++) {
			float x1 = static_cast<float>(x) - mid_xf - deltaX;
			float y1 = mid_yf - static_cast<float>(y) - deltaY;
			float x0 = x1 * cosa - y1 * sina;
			float y0 = x1 * sina + y1 * cosa;
			int src_x = mid_x + static_cast<int>(x0 * scaleT);
			int src_y = mid_y - static_cast<int>(y0 * scaleT);

			if (0 <= src_x && src_x < w && 0 <= src_y && src_y < h)
				ret[x + y * w] = src[src_x + src_y * w];
			else
				ret[x + y * w] = 0u;
		}
	}
}

IMGFUNCLIB_API void imgf::
bilinear_transform(const byte bgra32src[], byte dst[], uint wid, uint hgt, float deltaX, float deltaY, float angle, float scale)
{
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);

	int w = static_cast<int>(wid);
	int h = static_cast<int>(hgt);
	float mid_xf = static_cast<float>(wid) * 0.5f;
	float mid_yf = static_cast<float>(hgt) * 0.5f;
	float sina = std::sinf(angle);
	float cosa = std::cosf(angle);
	float scaleT = 1.0f / scale;

	/*
	 * 大部分和邻近插值那里一样，关键是插值的部分不一样。
	 */

	for (int x = 0; x < w; x++) {
		for (int y = 0; y < h; y++) {
			/* 计算的数据 */
			float x1 = static_cast<float>(x) - mid_xf - deltaX;
			float y1 = mid_yf - static_cast<float>(y) - deltaY;
			float x0 = x1 * cosa - y1 * sina;
			float y0 = x1 * sina + y1 * cosa;
			float xr = mid_xf + x0 * scaleT;
			float yr = mid_yf - y0 * scaleT;

			/* 结果数据 */
			int ymini = static_cast<int>(yr), ymaxi = ymini + 1;
			int xmini = static_cast<int>(xr), xmaxi = xmini + 1;
			float xy	= (xr - static_cast<float>(xmini)) * (yr - static_cast<float>(ymini));
			float xcy	= (static_cast<float>(xmaxi) - xr) * (yr - static_cast<float>(ymini));
			float xcyc	= (static_cast<float>(xmaxi) - xr) * (static_cast<float>(ymaxi) - yr);
			float xyc	= (xr - static_cast<float>(xmini)) * (static_cast<float>(ymaxi) - yr);

			/* 开始改颜色 */
			if (0 <= xmini && xmaxi < w && 0 <= ymini && ymaxi < h) {
				for (auto type = ColorType::Begin; type < ColorType::End; type = static_cast<ColorType>(type + 1))
					bgra32color(dst, wid, hgt, x, y, type)
						= static_cast<byte>
							( static_cast<float>(bgra32color(bgra32src, wid, hgt, xmaxi, ymaxi, type)) * xy
							+ static_cast<float>(bgra32color(bgra32src, wid, hgt, xmini, ymaxi, type)) * xcy
							+ static_cast<float>(bgra32color(bgra32src, wid, hgt, xmini, ymini, type)) * xcyc
							+ static_cast<float>(bgra32color(bgra32src, wid, hgt, xmaxi, ymini, type)) * xyc
							)
						;
			} else
				ret[x + y * wid] = 0u;
		}
	}
}

IMGFUNCLIB_API void imgf::
filter(const byte bgra32src[], byte dst[], uint wid, uint hgt, float mat[], uint mat_wid, uint mat_hgt)
{
	if (!(mat_wid & 1U && mat_hgt & 1U))
		return ; // TODO: 这里是异常

	/* 拷贝一份，用于复制透明通道，以及图像边缘 */
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);
	auto total = wid * hgt;
	for (auto i = 0U; i < total; i++)
		ret[i] = src[i];

	/* 对 B G R 三通道执行卷积 */
	auto x_begin = mat_wid >> 1, x_end = wid - x_begin;
	auto y_begin = mat_hgt >> 1, y_end = hgt - y_begin;
	auto type = ColorType::Begin;

	while(type + 1U < ColorType::End) 
	{
		for (auto y = y_begin; y < y_end; y++) {
			for (auto x = x_begin; x < x_end; x++) {
				
				float sum = 0.0f;
				auto i_begin = x - x_begin, i_end = i_begin + mat_wid;
				auto j_begin = y - y_begin, j_end = j_begin + mat_hgt;
				for (auto j = j_begin; j < j_end; j++) {
					auto delta_i = mat_wid * (j - j_begin);	 /* 微不足道的减少乘法的优化 */
					for (auto i = i_begin; i < i_end; i++)
						sum += mat[(i - i_begin) + delta_i] * static_cast<float>(bgra32color(bgra32src, wid, hgt, i, j, type));
				}

				bgra32color(dst, wid, hgt, x, y, type)
					= sum > 255.f
					? 255U
					: sum < 0.f
					? 0U
					: static_cast<byte>(sum);
			}
		}

		type = static_cast<ColorType>(type + 1);
	}

}

IMGFUNCLIB_API void imgf::
filter_i(const byte bgra32src[], byte dst[], uint wid, uint hgt, int mat[], uint mat_wid, uint mat_hgt)
{
	if (!(mat_wid & 1U && mat_hgt & 1U))
		return ; // TODO: 这里是异常

	/* 拷贝一份，用于复制透明通道，以及图像边缘 */
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);
	auto total = wid * hgt;
	for (auto i = 0U; i < total; i++)
		ret[i] = src[i];

	/* 对 B G R 三通道执行卷积 */
	auto x_begin = mat_wid >> 1, x_end = wid - x_begin;
	auto y_begin = mat_hgt >> 1, y_end = hgt - y_begin;
	auto type = ColorType::Begin;

	while(type + 1U < ColorType::End) 
	{
		for (auto y = y_begin; y < y_end; y++) {
			for (auto x = x_begin; x < x_end; x++) {
				
				int sum = 0;
				auto i_begin = x - x_begin, i_end = i_begin + mat_wid;
				auto j_begin = y - y_begin, j_end = j_begin + mat_hgt;
				for (auto j = j_begin; j < j_end; j++) {
					auto delta_i = mat_wid * (j - j_begin);	 /* 微不足道的减少乘法的优化 */
					for (auto i = i_begin; i < i_end; i++) 
						sum += mat[(i - i_begin) + delta_i] * bgra32color(bgra32src, wid, hgt, i, j, type);
				}

				bgra32color(dst, wid, hgt, x, y, type)
					= sum > 255
					? 255U
					: sum < 0
					? 0U
					: static_cast<byte>(sum);
			}
		}

		type = static_cast<ColorType>(type + 1);
	}

}

IMGFUNCLIB_API void imgf::
filter_roberts(const byte bgra32src[], byte dst[], uint wid, uint hgt)
{
	/* 拷贝一份，用于复制透明通道，以及图像边缘 */
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);
	auto total = wid * hgt;
	for (auto i = 0U; i < total; i++)
		ret[i] = src[i];

	/* 对 B G R 三通道执行操作 */
	auto x_begin = 1U, x_end = wid;
	auto y_begin = 1U, y_end = hgt;
	auto type = ColorType::Begin;

	while(type + 1U < ColorType::End) 
	{
		for (auto y = y_begin; y < y_end; y++) {
			for (auto x = x_begin; x < x_end; x++) {
				
				auto deltaAD
					= static_cast<int>(bgra32color(dst, wid, hgt, x - 1, y - 1, type))
					- static_cast<int>(bgra32color(dst, wid, hgt, x, y, type));
				auto deltaBC
					= static_cast<int>(bgra32color(dst, wid, hgt, x, y - 1, type))
					- static_cast<int>(bgra32color(dst, wid, hgt, x - 1, y, type));

				auto value = static_cast<int>(std::sqrt(deltaAD * deltaAD + deltaBC * deltaBC));

				bgra32color(dst, wid, hgt, x, y, type)
					= value > 255
					? 255U
					: value < 0
					? 0U
					: static_cast<byte>(value);
			}
		}
		type = static_cast<ColorType>(type + 1);
	}
}

IMGFUNCLIB_API void imgf::
filter_sobel(const byte bgra32src[], byte dst[], uint wid, uint hgt)
{
	/* 拷贝一份，用于复制透明通道，以及图像边缘 */
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);
	auto total = wid * hgt;
	for (auto i = 0U; i < total; i++)
		ret[i] = src[i];

	/* 对 B G R 三通道执行操作 */
	auto x_begin = 1U, x_end = wid - x_begin;
	auto y_begin = 1U, y_end = hgt - y_begin;
	auto type = ColorType::Begin;

	while(type + 1U < ColorType::End) 
	{
		for (auto y = y_begin; y < y_end; y++) {
			for (auto x = x_begin; x < x_end; x++) {
				
				auto M00 = static_cast<int>(bgra32color(dst, wid, hgt, x - 1, y - 1, type));
				auto M01 = static_cast<int>(bgra32color(dst, wid, hgt, x - 1, y, type));
				auto M02 = static_cast<int>(bgra32color(dst, wid, hgt, x - 1, y + 1, type));
				auto M10 = static_cast<int>(bgra32color(dst, wid, hgt, x, y - 1, type));
				auto M11 = static_cast<int>(bgra32color(dst, wid, hgt, x, y, type));
				auto M12 = static_cast<int>(bgra32color(dst, wid, hgt, x, y + 1, type));
				auto M20 = static_cast<int>(bgra32color(dst, wid, hgt, x + 1, y - 1, type));
				auto M21 = static_cast<int>(bgra32color(dst, wid, hgt, x + 1, y, type));
				auto M22 = static_cast<int>(bgra32color(dst, wid, hgt, x + 1, y + 1, type));

				auto Gx = (M20 + (M21 << 1) + M22) - (M00 + (M01 << 1) + M02);
				auto Gy = (M00 + (M10 << 1) + M20) - (M02 + (M12 << 1) + M22);

				auto value = static_cast<int>(std::sqrt(Gx * Gx + Gy * Gy));

				bgra32color(dst, wid, hgt, x, y, type)
					= value > 255
					? 255U
					: value < 0
					? 0U
					: static_cast<byte>(value);
			}
		}

		type = static_cast<ColorType>(type + 1);
	}
}

IMGFUNCLIB_API void imgf::
filter_median(const byte bgra32src[], byte dst[], uint wid, uint hgt)
{
	/* 拷贝一份，用于复制透明通道，以及图像边缘 */
	auto src = reinterpret_cast<const uint*>(bgra32src);
	auto ret = reinterpret_cast<uint*>(dst);
	auto total = wid * hgt;
	for (auto i = 0U; i < total; i++)
		ret[i] = src[i];

	/* 对 B G R 三通道执行操作 */
	int nums[9];

	auto x_begin = 1U, x_end = wid - 1U;
	auto y_begin = 1U, y_end = hgt - 1U;
	auto type = ColorType::Begin;

	while(type + 1U < ColorType::End) 
	{
		for (auto y = y_begin; y < y_end; y++) {
			for (auto x = x_begin; x < x_end; x++) {
				
				auto k = 0U;
				for (auto j = -1; j < 2; j++)
					for (auto i = -1; i < 2; i++) 
						nums[k++] = bgra32color(bgra32src, wid, hgt, x + i, y + j, type);

				std::nth_element(nums, nums + 4, nums + 9);
				auto value = nums[4];

				bgra32color(dst, wid, hgt, x, y, type)
					= value > 255
					? 255U
					: value < 0
					? 0U
					: static_cast<byte>(value);
			}
		}
		type = static_cast<ColorType>(type + 1);
	}
}

IMGFUNCLIB_API void imgf::
morphology_expand(const byte gray8src[], byte dst[], uint wid, uint hgt)
{
	static const uint mat_wid = 3;
	static const uint mat_hgt = 3;

	auto x_begin = mat_wid >> 1, x_end = wid - x_begin;
	auto y_begin = mat_hgt >> 1, y_end = hgt - y_begin;
	
	/* 拷贝一份，用于复制透明通道，以及图像边缘 */
	memset(dst, 0xFFU, sizeof(byte) * wid * hgt);

	/* 开始计算 */
	for (auto y = y_begin; y < y_end; y++) {
		auto j_begin = y - y_begin, j_end = j_begin + mat_hgt;
		
		for (auto x = x_begin; x < x_end; x++) {
			auto i_begin = x - x_begin, i_end = i_begin + mat_wid;

			for (auto j = j_begin; j < j_end; j++) {
				auto delta_i = j * wid;
				for (auto i = i_begin; i < i_end; i++)
					if (!gray8src[i + delta_i]) {
						dst[x + y * wid] = 0U;
						goto end_check;
					}
			}
		end_check:;
		}
	}
}

IMGFUNCLIB_API void imgf::
morphology_corrode(const byte gray8src[], byte dst[], uint wid, uint hgt)
{
	static const uint mat_wid = 3;
	static const uint mat_hgt = 3;

	auto x_begin = mat_wid >> 1, x_end = wid - x_begin;
	auto y_begin = mat_hgt >> 1, y_end = hgt - y_begin;
	
	/* 拷贝一份，用于复制透明通道，以及图像边缘 */
	memset(dst, 0U, sizeof(byte) * wid * hgt);

	/* 开始计算 */
	for (auto y = y_begin; y < y_end; y++) {
		auto j_begin = y - y_begin, j_end = j_begin + mat_hgt;
		
		for (auto x = x_begin; x < x_end; x++) {
			auto i_begin = x - x_begin, i_end = i_begin + mat_wid;

			for (auto j = j_begin; j < j_end; j++) {
				auto delta_i = j * wid;
				for (auto i = i_begin; i < i_end; i++)
					if (gray8src[i + delta_i]) {
						dst[x + y * wid] = 0xFFU;
						goto end_check;
					}
			}
		end_check:;
		}
	}

}

IMGFUNCLIB_API void imgf::
bgra32_to_gray8(const byte bgra32src[], byte gray8src[], uint wid, uint hgt)
{
	const int total = wid * hgt;
	for (auto i = 0U; i < total; i++) {
		uint idx_basic = i << 2U;
		uint b = bgra32src[idx_basic | ColorType::B];
		uint g = bgra32src[idx_basic | ColorType::G];
		uint r = bgra32src[idx_basic | ColorType::R];
		gray8src[i] = static_cast<byte>((r * 19595U + g * 38469U + b * 7472U) >> 16U);
	}
}


inline imgf::byte & imgf::
bgra32color(byte bgra32src[], uint wid, uint hgt, uint x, uint y, ColorType type)
{
	return bgra32src[((x + wid * y) << 2U) | type];
}

inline imgf::byte imgf::
bgra32color(const byte bgra32src[], uint wid, uint hgt, uint x, uint y, ColorType type)
{
	return bgra32src[((x + wid * y) << 2U) | type];
}





// 
// 附录
// 
// 1. 上文提到的用于打表 <quantize[9][256]> 的程序， 
// 
//#include <cstdio>
//#include <cmath>
//typedef unsigned int uint;
//uint quantize_helper(uint level, uint new_level, uint value)
//{
//	float times = static_cast<float>(level) / static_cast<float>(new_level);
//	uint ret = static_cast<uint>(std::round(static_cast<float>(value) / times) * static_cast<float>(times));
//	return ret;
//}
//void print_array(uint interval)
//{
//	const uint max = 256u;
//	putchar('{');
//	printf(" %3d", quantize_helper(max - 1U, interval - 1U, 0u));
//	for (auto i = 1u; i < max; i++) {
//		if (i % (80 / 5 - 1) == 0)
//			putchar('\n');
//		printf(", %3d", quantize_helper(max - 1U, interval - 1U, i));
//	}
//	putchar('}');
//}
//void print_array_of_array()
//{
//	const uint max = 256u;
//	putchar('{');
//	putchar('\n');
//	for (auto idx = 2u; idx < max; idx <<= 1u) {
//		print_array(idx);
//		putchar(',');
//		putchar('\n');
//	}
//	print_array(max);
//	putchar('\n');
//	putchar('}');
//	putchar('\n');
//}
//int main(void)
//{	
//	print_array_of_array();
//	return 0;
//}

