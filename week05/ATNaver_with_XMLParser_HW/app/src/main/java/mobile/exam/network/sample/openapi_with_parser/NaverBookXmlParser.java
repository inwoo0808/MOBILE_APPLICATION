package mobile.exam.network.sample.openapi_with_parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class NaverBookXmlParser {

    // XML에서 읽어들일 태그를 구분
    private enum TagType { NONE, TITLE, LINK, IMAGE, AUTHOR };

    // 파싱 대상인 태그를 상수로 선언
    private final static String ITEM_TAG = "item";
    private final static String TITLE_TAG = "title";
    private final static String LINK_TAG = "link";
    private final static String IMAGE_TAG = "image";
    private final static String AUTHOR_TAG = "author";

    private XmlPullParser parser;

    public NaverBookXmlParser() {
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NaverBookDto> parse(String xml) {
        ArrayList<NaverBookDto> resultList = new ArrayList<>();
        NaverBookDto nbd = null;
        TagType tagType = TagType.NONE; // 태그 구분하기 위한 enum 변수 초기화

        try {
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if(tag.equals(ITEM_TAG)) {
                            nbd = new NaverBookDto(); // 이때 dto 객체 생성
                        } else if (tag.equals(TITLE_TAG) && nbd != null) {
                            tagType = TagType.TITLE;
                        } else if (tag.equals(LINK_TAG) && nbd != null) {
                            tagType = TagType.LINK;
                        } else if (tag.equals(IMAGE_TAG) && nbd != null) {
                            tagType = TagType.IMAGE;
                        } else if (tag.equals(AUTHOR_TAG) && nbd != null) {
                            tagType = TagType.AUTHOR;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals(ITEM_TAG)) {
                            resultList.add(nbd);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case TITLE:
                                nbd.setTitle(parser.getText());
                                break;
                            case LINK:
                                nbd.setLink(parser.getText());
                                break;
                            case IMAGE:
                                nbd.setImageLink(parser.getText());
                                break;
                            case AUTHOR:
                                nbd.setAuthor(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        

        return resultList;
    }
}
