package com.shouzhi.service.customexception;

/**
 * 自定义异常-文件下载异常
 * @author WX
 * @date 2021-02-06 11:41:57
 */
public class FileDownloadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造一个没有详细信息的<code>FileDownloadException</code>
     */
    public FileDownloadException() {
        super();
    }

    /**
     * 使用指定的详细信息构造<code>FileDownloadException</code>
     * @param   s   the detail message.
     */
    public FileDownloadException(String s) {
        super(s);
    }

    /**
     * 使用指定的详细信息和原因构造新异常。
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link Throwable#getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value
     *         is permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since 1.5
     */
    // public FileDownloadException(String message, Throwable cause) {
    //     super(message, cause);
    // }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.5
     */
    // public FileDownloadException(Throwable cause) {
    //     super(cause);
    // }

}
