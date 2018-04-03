/*********************************************************************
 *	@file		HuffmanCoding.cpp
 *	@brief		关于哈夫曼编码
 *********************************************************************/

#include <queue>
#include <stack>
#include <vector>
#include <algorithm>
#include <functional>

#include "BitStack.hpp"
#include "BitStream.hpp"
#include "ImgFuncLib.hpp"


IMGFUNCLIB_API int imgf::
huffman_encode(const byte src[], uint src_size, byte dst[], uint dst_size) 
{
/* 定义的常量 */
	static const uint MAX_BYTE_NUM = 1U << (sizeof(byte) << 3U);
	static const uint TREE_BIT_SIZE = MAX_BYTE_NUM * (8U + 1U) + 2U * (MAX_BYTE_NUM - 1U) * 2U;
	static const uint HEADER_BIT_SIZE
		= (32U)										/* 文件开始标志的占用的位 */
		+ (32U)										/* 树长度占用的位 */
		+ (32U)										/* 二进制数据长度占用的位 */
		+ (TREE_BIT_SIZE)							/* 树数据块的位长度 */
		;
	static const uint DATA_MAX_BIT_SIZE
		= (src_size << 3U)							/* 二进制数据块的位长度 */
		;


	/* 计算缓冲区 最小 BIT 大小 */
	if (((HEADER_BIT_SIZE + 7U) >> 3U) > dst_size)
		return -1;
		// throw std::runtime_error("buffer size for 'huffman encode' is not enough.");

	BitStream bs;
	{	/* 打印头部 */
		bs << 0x3F3F3F3FU << TREE_BIT_SIZE << 0U;
	}

	/* K 叉树，节点 */
	struct Node {
		std::vector<Node> children;
		byte value;
		inline bool operator < (Node const & rhs) const { return value < rhs.value;}
	} root;

	{	/* 利用 K 叉树，构造哈夫曼树 */

		/* 统计每个数出现的频率 */
		uint cnt[MAX_BYTE_NUM] = {};
		for (auto i = 0U; i < src_size; i++)
			cnt[src[i]]++;

		/* 将原始节点加入到堆中 */
		typedef std::pair<uint, Node> WeightedNode;
		std::priority_queue<WeightedNode, std::vector<WeightedNode>, std::greater<WeightedNode>> heap;
		for (auto i = 0U; i < MAX_BYTE_NUM; i++)
			heap.push(std::make_pair(cnt[i], Node{ {}, static_cast<byte>(i) }));
	
		/* 开始合并节点 */
		while (heap.size() > 1U) {
			/* 父节点 */
			Node parent{ {}, 0U };
			parent.children.reserve(2U);

			uint weight = heap.top().first;
			parent.children.emplace_back(std::move(heap.top().second));
			heap.pop();
			
			weight += heap.top().first;
			parent.children.emplace_back(std::move(heap.top().second));
			heap.pop();

			heap.push(std::make_pair(weight, std::move(parent)));
		}

		root = std::move(heap.top().second);
		heap.pop();
	}

	{	/* 打印树到 bs */
		auto tree_size = 0U;
		std::stack<Node const *> stack;

		stack.push(&root);
		while (!stack.empty()) {
			/* 取栈顶 */
			auto ptr = stack.top();
			stack.pop();
			
			/* 判定是… */
			if (ptr == nullptr) {				/* 这是一个右括号 */
				bs << false << true;
				tree_size += 2U;
			} else {							/* 这是一个元素 */
				auto & node = *ptr;
				/* 检查是否为叶子节点 */
				if (node.children.empty()) {	/* 是叶子，则拷贝当前字符到列表 */
					bs << true << node.value;
					tree_size += 9U;
				} else {						/* 不是叶子节点，将其子节点加入到栈中 */
					stack.push(nullptr);		/* 右括号 */
					auto & childs = node.children;
					/* 倒着压入栈，这样取出的时候是顺序的 */
					for (auto riter = childs.rbegin(); riter != childs.rend(); riter++)
						stack.push(&*riter);	/* 子节点 */
					bs << false << false;		/* 左括号 */
					tree_size += 2U;
				}
			}
		}

		if (tree_size != TREE_BIT_SIZE)
			return -2;
			// throw std::runtime_error("unexcepted tree size.");
	}

	auto data_bit_size = 0U;
	{	/* 打印数据 */

		/* 以及 制作 [0, 255] 的二进制数的映射表 */
		std::vector<BitStack> table(MAX_BYTE_NUM);
		std::stack<std::pair<Node const *, BitStack>> stack;

		stack.push(std::make_pair(&root, BitStack{}));
		while (!stack.empty()) {
			/* 取栈顶 */
			auto & top = *stack.top().first;
			auto path = std::move(stack.top().second);
			stack.pop();

			/* 检查是否为叶子节点 */
			if (top.children.empty()) {
				/* 是叶子，则拷贝当前编码（路径）到列表 */
				table[top.value] = path;
			} else {
				/* 不是叶子节点，将其子节点加入到栈中 */
				uint i = 0U;
				for (auto & child : top.children) {
					path.push(i++ != 0U);			/* 约定：左孩子为 0，右孩子为 1 */
					stack.push(std::make_pair(&child, path));
					path.pop();
				}
			}
		}

		/* 根据映射表，将数据输出到 bs */
		for (auto i = 0U; i < src_size; i++) {
			auto & bitstack = table[src[i]];
			bs << bitstack;
			data_bit_size += bitstack.size();
		}
	}

	/* 统计文件长度，判断缓冲区是否够 */
	auto total_size = (HEADER_BIT_SIZE + data_bit_size + 7U) >> 3U;
	if (total_size > dst_size)
		return -3;
		// throw std::runtime_error("buffer size for 'huffman encode' is not enough.");

	/* 输出 */
	for (auto i = 0U; bs >> dst[i]; i++);			/* 输出到 dst */

	reinterpret_cast<uint *>(dst)[2] = data_bit_size;/* 修改数据域的长度 */
	return static_cast<int>(total_size);			/* 返回实际长度 */
}


IMGFUNCLIB_API int imgf::
huffman_decode(const byte src[], uint src_size, byte dst[], uint dst_size)
{
	/* 检查文件头 */
	if (reinterpret_cast<uint const *>(src)[0] != 0x3F3F3F3FU)
		return -1;

	/* 导入数据到流中 */
	BitStream bs;
	for (auto i = sizeof(uint) / sizeof(byte); i < src_size; i++)
		bs << src[i];

	/* 获取基础数据 */
	uint tree_bit_size = 0U, data_bit_size = 0U;
	bs >> tree_bit_size >> data_bit_size;
	
	/* K 叉树，节点 */
	struct Node {
		std::vector<Node> children;
		byte value;
		inline bool operator < (Node const & rhs) const { return value < rhs.value;}
	} root;

	{	/* 读取哈夫曼树 */
		static const uint TREE_DEEPTH_LIMIT = 257U; // 暂时不用
		bool bit = false;
		
		std::stack<Node *> stack;
		stack.push(&root);

		auto bit_cnt = 0U;
		while (!stack.empty() && bit_cnt < tree_bit_size) {
			bs >> bit;
			bit_cnt++;

			if (bit) {		/* 结点 */
				byte value;
				bs >> value;
				bit_cnt += 8U;
				stack.top()->children.emplace_back(std::move(Node{ {}, value }));
			} else {		/* 括号 */
				bool is_right = false;
				bs >> is_right;
				bit_cnt++;

				if (is_right) {	/* 右括号 */
					stack.pop();
				} else {
					auto & top_children = stack.top()->children;
					top_children.emplace_back();
					stack.push(&top_children.back());
				}
			}
		}

		if (bit_cnt != tree_bit_size)
			return -2;
			// throw std::runtime_error("unexcepted tree size.");
	}

	uint actual_data_size = 0U;
	{	/* 根据树中的数据，翻译 bs 中剩下的 data */
		bool bit = false;
		Node const * cur = &root.children.front();
		for (auto i = 0U; i < data_bit_size && bs; i++) {
			bs >> bit;
			cur = bit						/* 约定：左孩子为 0，右孩子为 1 */
				? &cur->children.back()
				: &cur->children.front()
				;
			if (cur->children.empty()) {				/* 这是叶子节点 */
				if (actual_data_size > dst_size)
					return -3;
					// throw std::runtime_error("data size is not enough.");

				dst[actual_data_size++] = cur->value;	/* 保存数据 */
				cur = &root.children.front();			/* 重置 */
			}
		}
		
		/* 判断数据域是否完整 */
		if (cur != &root.children.front())
			return -4;
			// throw std::runtime_error("data is incomplete.");
	}

	return static_cast<int>(actual_data_size);
}
