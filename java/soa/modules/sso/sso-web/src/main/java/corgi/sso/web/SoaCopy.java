package corgi.sso.web;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by huanghuanlai on 16/6/25.
 */
public class SoaCopy {

    private static Integer count = 0;
    private static final String UTF_8 = "utf-8";
    public static void main(String[] args){
        String oldName = "sso";
        String renameName = "新名称";
        String path = "/Users/huanghuanlai/java/dounine/oschina/corgi/java/soa/systems/"+oldName;
        File file = new File(path);
        child(file,renameName,oldName);
        System.out.println("替换总数:"+count);
    }

    public static void child(File parent,String renameName,String oldName){
        String[] gs = {"xml","java","json","jsp","js"};
        for(File file : parent.listFiles()){
            if(!file.isDirectory()){
                String prefix =  FilenameUtils.getExtension(file.getPath());
                if(null!=prefix){
                    for(String g : gs){
                        if(g.equals(prefix)){
                            try {
                                String context = FileUtils.readFileToString(file, String.valueOf(Charset.forName(UTF_8)));
                                String contextTmp = context.replace(oldName,renameName);
                                if(!context.equals(contextTmp)){
                                    count++;
                                }
                                FileUtils.writeStringToFile(file,contextTmp, String.valueOf(Charset.forName(UTF_8)),false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
            if(file.getName().indexOf(oldName)>-1){
                File newFile = new File(file.getParentFile().getAbsoluteFile()+"/"+file.getName().replace(oldName,renameName));
                file.renameTo(newFile);
                count++;
                if(newFile.isDirectory()){
                    child(newFile,renameName,oldName);
                }
            }else{
                if(file.isDirectory()){
                    child(file,renameName,oldName);
                }
            }
        }
    }
}
