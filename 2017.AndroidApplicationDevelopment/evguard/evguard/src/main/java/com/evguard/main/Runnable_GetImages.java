package com.evguard.main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.evguard.image.ImageFolder;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/***
 * 获取图片
 * @author wlh
 *
 */
public class Runnable_GetImages implements Runnable{

	private Context mContext;
	private ContentResolver mContentResolver;
	private OnGetImagesEventListener mOnGetImagesListener;
	/** 
     * 临时的辅助类，用于防止同一个文件夹的多次扫描 
     */  
    private HashSet<String> mDirPaths = new HashSet<String>(); 
    /**
	 * 图片数量最多的文件夹
	 */
	private ImageFolder mImgDir;

	private List<ImageFolder> folders;

	
	private int picmaxsize=0;
	
	public Runnable_GetImages(Context context,OnGetImagesEventListener onGetImagesListener)
	{
		this.mContext=context;
		this.mOnGetImagesListener=onGetImagesListener;
	}
	
	@Override
	public void run() {

		if(mOnGetImagesListener!=null)mOnGetImagesListener.onGetingImages();
		mContentResolver = mContext.getContentResolver();
		if(folders==null)folders=new ArrayList<ImageFolder>();
		folders.clear();
		//扫描内存
		getImageByUri(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		//扫描sd卡
		if (Environment.getExternalStorageState().equals(  
        Environment.MEDIA_MOUNTED)){
			getImageByUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		mDirPaths.clear() ;
		if(mOnGetImagesListener!=null)mOnGetImagesListener.onGetImagesCompeleted(mImgDir, folders);
	}
	private void getImageByUri(Uri mImageUri)
	{
		try {
			// 只查询jpeg和png的图片  
			Cursor mCursor = mContentResolver.query(mImageUri, null,  
			        MediaStore.Images.Media.MIME_TYPE + "=? or "  
			                + MediaStore.Images.Media.MIME_TYPE + "=?",  
			        new String[] { "image/jpeg", "image/png" },  
			        MediaStore.Images.Media.DATE_MODIFIED); 
			if(mCursor!=null)
			{
				while(mCursor.moveToNext())
				{
					// 获取图片的路径  
			        String path = mCursor.getString(mCursor  
			                .getColumnIndex(MediaStore.Images.Media.DATA));  
			        File parentfile=new File(path).getParentFile();
			        if(parentfile==null)continue;
			        String parentdir=parentfile.getAbsolutePath();
			        if(mDirPaths.contains(parentdir)){continue;}
			        mDirPaths.add(parentdir);
			        ImageFolder afolder=new ImageFolder();
			        afolder.setFirstImagePath(path);
			        afolder.setFolderDir(parentdir);
			        afolder.setFolderName(parentfile.getName());
			        if(parentfile.list()==null)continue;
			        int picSize = parentfile.list(new FilenameFilter()  
			        {  
			            @Override  
			            public boolean accept(File dir, String filename)  
			            {  
			                if (filename.endsWith(".jpg")  
			                        || filename.endsWith(".png")  
			                        || filename.endsWith(".jpeg"))  
			                    return true;  
			                return false;  
			            }  
			        }).length;  
			        afolder.setFolderImageCount(picSize);
			        if(picmaxsize<picSize)
			        {
			        	picmaxsize=picSize;
			        	mImgDir=afolder;
			        }
			        folders.add(afolder);
				}
				mCursor.close();  
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public interface OnGetImagesEventListener{
		public void onGetingImages();
		public void onGetImagesCompeleted(ImageFolder maxImageFolder,List<ImageFolder> alist);
	}

}
