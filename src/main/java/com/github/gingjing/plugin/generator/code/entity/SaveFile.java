package com.github.gingjing.plugin.generator.code.entity;

import com.github.gingjing.plugin.generator.code.tool.FileUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.LightVirtualFile;

import java.util.StringJoiner;

/**
 * 需要保存的文件
 *
 * @author makejava
 * @version 1.0.0
 * @since 2020/04/20 22:54
 */
public class SaveFile {
    private static final Logger LOG = Logger.getInstance(SaveFile.class);
    /**
     * 所属项目
     */
    private Project project;
    /**
     * 文件保存目录
     */
    private String path;
    /**
     * 虚拟文件
     */
    private VirtualFile virtualFile;
    /**
     * 需要保存的文件
     */
    private PsiFile file;
    /**
     * 是否需要重新格式化代码
     */
    private boolean reformat;
    /**
     * 文件
     */
    private PsiFile psiFile;
    /**
     * 是否显示操作提示
     */
    private boolean operateTitle;

    /**
     * 构建对象
     *
     * @param path     路径
     * @param fileName 文件没
     * @param reformat 是否重新格式化代码
     */
    public SaveFile(Project project, String path, String fileName, String content, boolean reformat, boolean operateTitle) {
        this.path = path;
        this.project = project;
        // 构建文件对象
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        LOG.assertTrue(content != null);
        FileType fileType = FileTypeManager.getInstance().getFileTypeByFileName(fileName);
        // 换行符统一使用\n
        this.file = psiFileFactory.createFileFromText(fileName, fileType, content.replace("\r", ""));
        this.virtualFile = new LightVirtualFile(fileName, fileType, content.replace("\r", ""));
        this.reformat = reformat;
        this.operateTitle = operateTitle;
    }

    /**
     * 写入文件
     */
    public void write() {
        FileUtils.getInstance().write(this);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public PsiFile getFile() {
        return file;
    }

    public void setFile(PsiFile file) {
        this.file = file;
    }

    public boolean isReformat() {
        return reformat;
    }

    public void setReformat(boolean reformat) {
        this.reformat = reformat;
    }

    public PsiFile getPsiFile() {
        return psiFile;
    }

    public void setPsiFile(PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    public boolean isOperateTitle() {
        return operateTitle;
    }

    public void setOperateTitle(boolean operateTitle) {
        this.operateTitle = operateTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SaveFile saveFile = (SaveFile) o;

        if (reformat != saveFile.reformat) return false;
        if (operateTitle != saveFile.operateTitle) return false;
        if (project != null ? !project.equals(saveFile.project) : saveFile.project != null) return false;
        if (path != null ? !path.equals(saveFile.path) : saveFile.path != null) return false;
        if (virtualFile != null ? !virtualFile.equals(saveFile.virtualFile) : saveFile.virtualFile != null)
            return false;
        if (file != null ? !file.equals(saveFile.file) : saveFile.file != null) return false;
        return psiFile != null ? psiFile.equals(saveFile.psiFile) : saveFile.psiFile == null;
    }

    @Override
    public int hashCode() {
        int result = project != null ? project.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (virtualFile != null ? virtualFile.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (reformat ? 1 : 0);
        result = 31 * result + (psiFile != null ? psiFile.hashCode() : 0);
        result = 31 * result + (operateTitle ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SaveFile.class.getSimpleName() + "[", "]")
                .add("project=" + project)
                .add("path='" + path + "'")
                .add("virtualFile=" + virtualFile)
                .add("file=" + file)
                .add("reformat=" + reformat)
                .add("psiFile=" + psiFile)
                .add("operateTitle=" + operateTitle)
                .toString();
    }
}
