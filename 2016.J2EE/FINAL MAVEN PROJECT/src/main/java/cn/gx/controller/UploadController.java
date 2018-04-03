package cn.gx.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/upload")
public class UploadController {

	@ResponseBody
	@RequestMapping(value = "/images", method = RequestMethod.POST)
	public String uploadImage(MultipartHttpServletRequest request) {

		/* 链接路径 */
		String href = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath()
				+ "/upload/images/";
		/* 服务器文件路径 */
		String dir = request.getSession().getServletContext()
				.getRealPath("/upload/images/") + File.separator;
		/* 返回的 JSON 串 */
		String ret = new String();

		/* 开始上传 */
		List<MultipartFile> files = request.getFiles("file");
		for (MultipartFile file : files) {
			try {

				String oldName = file.getOriginalFilename();
				String suffix = oldName.substring(oldName.lastIndexOf("."));
				String newFileName = getNewFileName(suffix);
				File newFile = new File(dir + newFileName);
				file.transferTo(newFile);
				newFile.createNewFile();

				ret += ("{\"success\":true, \"file_path\":\"" + href
						+ newFileName + "\"}");
			} catch (Exception e) {
				e.printStackTrace();
				ret += ("{\"success\":false}");
			}
		}

		System.out.println("ret:" + ret);
		return ret;
	}

	private String getNewFileName(String suffix) {
		return UUID.randomUUID().toString() + suffix;
	}
}
