package org.whirlplatform.server.form.captcha;

import com.google.inject.Singleton;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.whirlplatform.meta.shared.AppConstant;

/**
 * генератор картинок капчи
 */
@Singleton
public class CaptchaImgServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    HttpSession session;

    ServletContext context = null;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        session = request.getSession(true);

        if (request.getHeader("User-Agent").indexOf("MSIE 6.0") == -1) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("cache-control",
                "max-age=0,no-cache, no-store,must-revalidate, proxy-revalidate, s-maxage=0");
        }
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        String sWidth = request.getParameter("w");
        String sHeight = request.getParameter("h");
        String sLettersCount = request.getParameter("l");
        Integer lettersCount = null;
        if (sLettersCount != null) {
            try {
                lettersCount = Integer.parseInt(sLettersCount);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        CaptchaImgGenerator captchaImgGenerator = null;
        if (sWidth != null && sHeight != null) {
            try {
                int width = Integer.parseInt(sWidth);
                int height = Integer.parseInt(sHeight);
                captchaImgGenerator = new CaptchaImgGenerator(height, width, lettersCount,
                    response.getOutputStream());
            } catch (NumberFormatException nfe) {
                captchaImgGenerator =
                    new CaptchaImgGenerator(lettersCount, response.getOutputStream());
            }
        } else {
            captchaImgGenerator = new CaptchaImgGenerator(lettersCount, response.getOutputStream());
        }

        String code = captchaImgGenerator.getVerificationValue();

        // получить из параметра запроса
        // String componentId =
        // request.getParameter(CaptchaBuilder.CAPTCHA_KEY);
        String componentId = request.getParameter("cid");
        if (componentId == null) {
            componentId = "";
        }
        String key = AppConstant.CAPTCHA_SESSION_KEY + componentId;
        session.setAttribute(key, code);
        response.setContentLength(captchaImgGenerator.getBufferLength());
        response.flushBuffer();
    }
}
