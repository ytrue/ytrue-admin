package com.ytrue.infra.storage.utils;

/**
 * @author ytrue
 * @date 2023-09-09 17:09
 * @description ContentTypeUtil
 */


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hanjinqun
 * @date 2022/5/17
 * 文件后缀名及Content-Type关系转换工具类
 */
public class ContentTypeUtil {
    private static final Map<String, String> MAP = new HashMap<>();

    static {
        MAP.put("other", "application/octet-stream");
        MAP.put(".tif", "image/tiff");
        MAP.put("0.001", "application/x-001");
        MAP.put("0.301", "application/x-301");
        MAP.put("0.323", "text/h323");
        MAP.put("0.906", "application/x-906");
        MAP.put("0.907", "drawing/907");
        MAP.put(".a11", "application/x-a11");
        MAP.put(".acp", "audio/x-mei-aac");
        MAP.put(".ai", "application/postscript");
        MAP.put(".aif", "audio/aiff");
        MAP.put(".aifc", "audio/aiff");
        MAP.put(".aiff", "audio/aiff");
        MAP.put(".anv", "application/x-anv");
        MAP.put(".asa", "text/asa");
        MAP.put(".asf", "video/x-ms-asf");
        MAP.put(".asp", "text/asp");
        MAP.put(".asx", "video/x-ms-asf");
        MAP.put(".au", "audio/basic");
        MAP.put(".avi", "video/avi");
        MAP.put(".awf", "application/vnd.adobe.workflow");
        MAP.put(".biz", "text/xml");
        MAP.put(".bmp", "application/x-bmp");
        MAP.put(".bot", "application/x-bot");
        MAP.put(".c4t", "application/x-c4t");
        MAP.put(".c90", "application/x-c90");
        MAP.put(".cal", "application/x-cals");
        MAP.put(".cat", "application/vnd.ms-pki.seccat");
        MAP.put(".cdf", "application/x-netcdf");
        MAP.put(".cdr", "application/x-cdr");
        MAP.put(".cel", "application/x-cel");
        MAP.put(".cer", "application/x-x509-ca-cert");
        MAP.put(".cg4", "application/x-g4");
        MAP.put(".cgm", "application/x-cgm");
        MAP.put(".cit", "application/x-cit");
        MAP.put(".class", "java/");
        MAP.put(".cml", "text/xml");
        MAP.put(".cmp", "application/x-cmp");
        MAP.put(".cmx", "application/x-cmx");
        MAP.put(".cot", "application/x-cot");
        MAP.put(".crl", "application/pkix-crl");
        MAP.put(".crt", "application/x-x509-ca-cert");
        MAP.put(".csi", "application/x-csi");
        MAP.put(".css", "text/css");
        MAP.put(".cut", "application/x-cut");
        MAP.put(".dbf", "application/x-dbf");
        MAP.put(".dbm", "application/x-dbm");
        MAP.put(".dbx", "application/x-dbx");
        MAP.put(".dcd", "text/xml");
        MAP.put(".dcx", "application/x-dcx");
        MAP.put(".der", "application/x-x509-ca-cert");
        MAP.put(".dgn", "application/x-dgn");
        MAP.put(".dib", "application/x-dib");
        MAP.put(".dll", "application/x-msdownload");
        MAP.put(".doc", "application/msword");
        MAP.put(".dot", "application/msword");
        MAP.put(".drw", "application/x-drw");
        MAP.put(".dtd", "text/xml");
        MAP.put(".dwf", "Model/vnd.dwf");
        //MAP.put(".dwf","application/x-dwf");
        MAP.put(".dwg", "application/x-dwg");
        MAP.put(".dxb", "application/x-dxb");
        MAP.put(".dxf", "application/x-dxf");
        MAP.put(".edn", "application/vnd.adobe.edn");
        MAP.put(".emf", "application/x-emf");
        MAP.put(".eml", "message/rfc822");
        MAP.put(".ent", "text/xml");
        MAP.put(".epi", "application/x-epi");
        MAP.put(".eps", "application/x-ps");
        //MAP.put(".eps","application/postscript");
        MAP.put(".etd", "application/x-ebx");
        MAP.put(".exe", "application/x-msdownload");
        MAP.put(".fax", "image/fax");
        MAP.put(".fdf", "application/vnd.fdf");
        MAP.put(".fif", "application/fractals");
        MAP.put(".fo", "text/xml");
        MAP.put(".frm", "application/x-frm");
        MAP.put(".g4", "application/x-g4");
        MAP.put(".gbr", "application/x-gbr");
        MAP.put(".", "application/x-");
        MAP.put(".gif", "image/gif");
        MAP.put(".gl2", "application/x-gl2");
        MAP.put(".gp4", "application/x-gp4");
        MAP.put(".hgl", "application/x-hgl");
        MAP.put(".hmr", "application/x-hmr");
        MAP.put(".hpg", "application/x-hpgl");
        MAP.put(".hpl", "application/x-hpl");
        MAP.put(".hqx", "application/mac-binhex40");
        MAP.put(".hrf", "application/x-hrf");
        MAP.put(".hta", "application/hta");
        MAP.put(".htc", "text/x-component");
        MAP.put(".htm", "text/html");
        MAP.put(".html", "text/html");
        MAP.put(".htt", "text/webviewhtml");
        MAP.put(".htx", "text/html");
        MAP.put(".icb", "application/x-icb");
        MAP.put(".ico", "image/x-icon");
        //MAP.put(".ico","application/x-ico");
        MAP.put(".iff", "application/x-iff");
        MAP.put(".ig4", "application/x-g4");
        MAP.put(".igs", "application/x-igs");
        MAP.put(".iii", "application/x-iphone");
        MAP.put(".img", "application/x-img");
        MAP.put(".ins", "application/x-internet-signup");
        MAP.put(".isp", "application/x-internet-signup");
        MAP.put(".IVF", "video/x-ivf");
        MAP.put(".java", "java/*");
        MAP.put(".jpg", "image/jpeg");
        //MAP.put(".jfif", "image/jpeg");
        //MAP.put(".jpe", "image/jpeg");
        //MAP.put(".jpe","application/x-jpe");
        MAP.put(".jpeg", "image/jpeg");
        //MAP.put(".jpg","application/x-jpg");
        MAP.put(".js", "application/x-javascript");
        MAP.put(".jsp", "text/html");
        MAP.put(".la1", "audio/x-liquid-file");
        MAP.put(".lar", "application/x-laplayer-reg");
        MAP.put(".latex", "application/x-latex");
        MAP.put(".lavs", "audio/x-liquid-secure");
        MAP.put(".lbm", "application/x-lbm");
        MAP.put(".lmsff", "audio/x-la-lms");
        MAP.put(".ls", "application/x-javascript");
        MAP.put(".ltr", "application/x-ltr");
        MAP.put(".m1v", "video/x-mpeg");
        MAP.put(".m2v", "video/x-mpeg");
        MAP.put(".m3u", "audio/mpegurl");
        MAP.put(".m4e", "video/mpeg4");
        MAP.put(".mac", "application/x-mac");
        MAP.put(".man", "application/x-troff-man");
        MAP.put(".math", "text/xml");
        MAP.put(".mdb", "application/msaccess");
        //MAP.put(".mdb","application/x-mdb");
        MAP.put(".mfp", "application/x-shockwave-flash");
        MAP.put(".mht", "message/rfc822");
        MAP.put(".mhtml", "message/rfc822");
        MAP.put(".mi", "application/x-mi");
        MAP.put(".mid", "audio/mid");
        MAP.put(".midi", "audio/mid");
        MAP.put(".mil", "application/x-mil");
        MAP.put(".mml", "text/xml");
        MAP.put(".mnd", "audio/x-musicnet-download");
        MAP.put(".mns", "audio/x-musicnet-stream");
        MAP.put(".mocha", "application/x-javascript");
        MAP.put(".movie", "video/x-sgi-movie");
        MAP.put(".mp1", "audio/mp1");
        MAP.put(".mp2", "audio/mp2");
        MAP.put(".mp2v", "video/mpeg");
        MAP.put(".mp3", "audio/mp3");
        MAP.put(".mp4", "video/mpeg4");
        MAP.put(".mpa", "video/x-mpg");
        MAP.put(".mpd", "application/vnd.ms-project");
        MAP.put(".mpe", "video/x-mpeg");
        MAP.put(".mpeg", "video/mpg");
        MAP.put(".mpg", "video/mpg");
        MAP.put(".mpga", "audio/rn-mpeg");
        MAP.put(".mpp", "application/vnd.ms-project");
        MAP.put(".mps", "video/x-mpeg");
        MAP.put(".mpt", "application/vnd.ms-project");
        MAP.put(".mpv", "video/mpg");
        MAP.put(".mpv2", "video/mpeg");
        MAP.put(".mpw", "application/vnd.ms-project");
        MAP.put(".mpx", "application/vnd.ms-project");
        MAP.put(".mtx", "text/xml");
        MAP.put(".mxp", "application/x-mmxp");
        MAP.put(".net", "image/pnetvue");
        MAP.put(".nrf", "application/x-nrf");
        MAP.put(".nws", "message/rfc822");
        MAP.put(".odc", "text/x-ms-odc");
        MAP.put(".out", "application/x-out");
        MAP.put(".p10", "application/pkcs10");
        MAP.put(".p12", "application/x-pkcs12");
        MAP.put(".p7b", "application/x-pkcs7-certificates");
        MAP.put(".p7c", "application/pkcs7-mime");
        MAP.put(".p7m", "application/pkcs7-mime");
        MAP.put(".p7r", "application/x-pkcs7-certreqresp");
        MAP.put(".p7s", "application/pkcs7-signature");
        MAP.put(".pc5", "application/x-pc5");
        MAP.put(".pci", "application/x-pci");
        MAP.put(".pcl", "application/x-pcl");
        MAP.put(".pcx", "application/x-pcx");
        MAP.put(".pdf", "application/pdf");
        //MAP.put(".pdf","application/pdf");
        MAP.put(".pdx", "application/vnd.adobe.pdx");
        MAP.put(".pfx", "application/x-pkcs12");
        MAP.put(".pgl", "application/x-pgl");
        MAP.put(".pic", "application/x-pic");
        MAP.put(".pko", "application/vnd.ms-pki.pko");
        MAP.put(".pl", "application/x-perl");
        MAP.put(".plg", "text/html");
        MAP.put(".pls", "audio/scpls");
        MAP.put(".plt", "application/x-plt");
        MAP.put(".png", "image/png");
        //MAP.put(".png","application/x-png");
        MAP.put(".pot", "application/vnd.ms-powerpoint");
        MAP.put(".ppa", "application/vnd.ms-powerpoint");
        MAP.put(".ppm", "application/x-ppm");
        MAP.put(".pps", "application/vnd.ms-powerpoint");
        MAP.put(".ppt", "application/vnd.ms-powerpoint");
        //MAP.put(".ppt","application/x-ppt");
        MAP.put(".pr", "application/x-pr");
        MAP.put(".prf", "application/pics-rules");
        MAP.put(".prn", "application/x-prn");
        MAP.put(".prt", "application/x-prt");
        MAP.put(".ps", "application/x-ps");
        //MAP.put(".ps","application/postscript");
        MAP.put(".ptn", "application/x-ptn");
        MAP.put(".pwz", "application/vnd.ms-powerpoint");
        MAP.put(".r3t", "text/vnd.rn-realtext3d");
        MAP.put(".ra", "audio/vnd.rn-realaudio");
        MAP.put(".ram", "audio/x-pn-realaudio");
        MAP.put(".ras", "application/x-ras");
        MAP.put(".rat", "application/rat-file");
        MAP.put(".rdf", "text/xml");
        MAP.put(".rec", "application/vnd.rn-recording");
        MAP.put(".red", "application/x-red");
        MAP.put(".rgb", "application/x-rgb");
        MAP.put(".rjs", "application/vnd.rn-realsystem-rjs");
        MAP.put(".rjt", "application/vnd.rn-realsystem-rjt");
        MAP.put(".rlc", "application/x-rlc");
        MAP.put(".rle", "application/x-rle");
        MAP.put(".rm", "application/vnd.rn-realmedia");
        MAP.put(".rmf", "application/vnd.adobe.rmf");
        MAP.put(".rmi", "audio/mid");
        MAP.put(".rmj", "application/vnd.rn-realsystem-rmj");
        MAP.put(".rmm", "audio/x-pn-realaudio");
        MAP.put(".rmp", "application/vnd.rn-rn_music_package");
        MAP.put(".rms", "application/vnd.rn-realmedia-secure");
        MAP.put(".rmvb", "application/vnd.rn-realmedia-vbr");
        MAP.put(".rmx", "application/vnd.rn-realsystem-rmx");
        MAP.put(".rnx", "application/vnd.rn-realplayer");
        MAP.put(".rp", "image/vnd.rn-realpix");
        MAP.put(".rpm", "audio/x-pn-realaudio-plugin");
        MAP.put(".rsml", "application/vnd.rn-rsml");
        MAP.put(".rt", "text/vnd.rn-realtext");
        MAP.put(".rtf", "application/msword");
        //MAP.put(".rtf","application/x-rtf");
        MAP.put(".rv", "video/vnd.rn-realvideo");
        MAP.put(".sam", "application/x-sam");
        MAP.put(".sat", "application/x-sat");
        MAP.put(".sdp", "application/sdp");
        MAP.put(".sdw", "application/x-sdw");
        MAP.put(".sit", "application/x-stuffit");
        MAP.put(".slb", "application/x-slb");
        MAP.put(".sld", "application/x-sld");
        MAP.put(".slk", "drawing/x-slk");
        MAP.put(".smi", "application/smil");
        MAP.put(".smil", "application/smil");
        MAP.put(".smk", "application/x-smk");
        MAP.put(".snd", "audio/basic");
        MAP.put(".sol", "text/plain");
        MAP.put(".sor", "text/plain");
        MAP.put(".spc", "application/x-pkcs7-certificates");
        MAP.put(".spl", "application/futuresplash");
        MAP.put(".spp", "text/xml");
        MAP.put(".ssm", "application/streamingmedia");
        MAP.put(".sst", "application/vnd.ms-pki.certstore");
        MAP.put(".stl", "application/vnd.ms-pki.stl");
        MAP.put(".stm", "text/html");
        MAP.put(".sty", "application/x-sty");
        MAP.put(".svg", "text/xml");
        MAP.put(".swf", "application/x-shockwave-flash");
        MAP.put(".tdf", "application/x-tdf");
        MAP.put(".tg4", "application/x-tg4");
        MAP.put(".tga", "application/x-tga");
        //MAP.put(".tif","image/tiff");
        //MAP.put(".tif","application/x-tif");
        MAP.put(".tiff", "image/tiff");
        MAP.put(".tld", "text/xml");
        MAP.put(".top", "drawing/x-top");
        MAP.put(".torrent", "application/x-bittorrent");
        MAP.put(".tsd", "text/xml");
        MAP.put(".txt", "text/plain");
        MAP.put(".uin", "application/x-icq");
        MAP.put(".uls", "text/iuls");
        MAP.put(".vcf", "text/x-vcard");
        MAP.put(".vda", "application/x-vda");
        MAP.put(".vdx", "application/vnd.visio");
        MAP.put(".vml", "text/xml");
        MAP.put(".vpg", "application/x-vpeg005");
        MAP.put(".vsd", "application/vnd.visio");
        //MAP.put(".vsd","application/x-vsd");
        MAP.put(".vss", "application/vnd.visio");
        MAP.put(".vst", "application/vnd.visio");
        //MAP.put(".vst","application/x-vst");
        MAP.put(".vsw", "application/vnd.visio");
        MAP.put(".vsx", "application/vnd.visio");
        MAP.put(".vtx", "application/vnd.visio");
        MAP.put(".vxml", "text/xml");
        MAP.put(".wav", "audio/wav");
        MAP.put(".wax", "audio/x-ms-wax");
        MAP.put(".wb1", "application/x-wb1");
        MAP.put(".wb2", "application/x-wb2");
        MAP.put(".wb3", "application/x-wb3");
        MAP.put(".wbmp", "image/vnd.wap.wbmp");
        MAP.put(".wiz", "application/msword");
        MAP.put(".wk3", "application/x-wk3");
        MAP.put(".wk4", "application/x-wk4");
        MAP.put(".wkq", "application/x-wkq");
        MAP.put(".wks", "application/x-wks");
        MAP.put(".wm", "video/x-ms-wm");
        MAP.put(".wma", "audio/x-ms-wma");
        MAP.put(".wmd", "application/x-ms-wmd");
        MAP.put(".wmf", "application/x-wmf");
        MAP.put(".wml", "text/vnd.wap.wml");
        MAP.put(".wmv", "video/x-ms-wmv");
        MAP.put(".wmx", "video/x-ms-wmx");
        MAP.put(".wmz", "application/x-ms-wmz");
        MAP.put(".wp6", "application/x-wp6");
        MAP.put(".wpd", "application/x-wpd");
        MAP.put(".wpg", "application/x-wpg");
        MAP.put(".wpl", "application/vnd.ms-wpl");
        MAP.put(".wq1", "application/x-wq1");
        MAP.put(".wr1", "application/x-wr1");
        MAP.put(".wri", "application/x-wri");
        MAP.put(".wrk", "application/x-wrk");
        MAP.put(".ws", "application/x-ws");
        MAP.put(".ws2", "application/x-ws");
        MAP.put(".wsc", "text/scriptlet");
        MAP.put(".wsdl", "text/xml");
        MAP.put(".wvx", "video/x-ms-wvx");
        MAP.put(".xdp", "application/vnd.adobe.xdp");
        MAP.put(".xdr", "text/xml");
        MAP.put(".xfd", "application/vnd.adobe.xfd");
        MAP.put(".xfdf", "application/vnd.adobe.xfdf");
        MAP.put(".xhtml", "text/html");
        MAP.put(".xls", "application/vnd.ms-excel");
        //MAP.put(".xls","application/x-xls");
        MAP.put(".xlw", "application/x-xlw");
        MAP.put(".xml", "text/xml");
        MAP.put(".xpl", "audio/scpls");
        MAP.put(".xq", "text/xml");
        MAP.put(".xql", "text/xml");
        MAP.put(".xquery", "text/xml");
        MAP.put(".xsd", "text/xml");
        MAP.put(".xsl", "text/xml");
        MAP.put(".xslt", "text/xml");
        MAP.put(".xwd", "application/x-xwd");
        MAP.put(".x_b", "application/x-x_b");
        MAP.put(".sis", "application/vnd.symbian.install");
        MAP.put(".sisx", "application/vnd.symbian.install");
        MAP.put(".x_t", "application/x-x_t");
        MAP.put(".ipa", "application/vnd.iphone");
        MAP.put(".apk", "application/vnd.android.package-archive");
        MAP.put(".xap", "application/x-silverlight-app");
    }

    /**
     * 、
     * 根据文件后缀名获取contentType
     */
    public static String getContentType(String type) {
        type = type.contains(".") ? type : "." + type;
        String contentType = MAP.get(type);
        if (StrUtil.isEmpty(contentType)) {
            contentType = MAP.get("other");
        }
        return contentType;
    }

    /**
     * 获取文件类型
     *
     * @param value
     * @return
     */
    public static List<String> getFileType(String value) {
        List<String> keyList = new ArrayList<>();
        for (String key : MAP.keySet()) {
            if (MAP.get(key).equals(value)) {
                keyList.add(key);
            }
        }

        if (CollUtil.isEmpty(keyList)) {
            keyList.set(0, "");
        }
        return keyList;
    }
}
