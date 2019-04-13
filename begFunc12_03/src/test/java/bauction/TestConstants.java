package bauction;

import java.math.BigDecimal;

public class TestConstants {
    public static final String INVALID_ID="invalid_id";


    public static final String TEST_USER_USERNAME ="Ivan";
    public static final String TEST_USER_FULL_NAME ="Ivan Ivanov";
    public static final String TEST_USER_EMAIL ="van40@vsi4ko.bg";
    public static final String TEST_USER_PASSWORD ="1234";

    public static final String CATEGORY="Allgemein";
    public static final String TOWN="Sofia";

    public static final String PRODUCT_NAME="Windsutzscheibe";
    public static final String PRODUCT_NAME_OTHER="Bremse";

    public static final String PICTURE_PATH="/images/some.jpg";

    public static final String PRODUCT_DESCRIPTION="Sehr gut gehaltene";
    public static final String PRODUCT_DESCRIPTION_OTHER="Flambant neuf";

    public static final BigDecimal AUCTION_WANTED_PRICE=BigDecimal.TEN;
    public static final BigDecimal AUCTION_REACHED_PRICE=BigDecimal.ONE;
    public static final String AUCTION_TYPE="Standard";

    public static final BigDecimal OFFER_PRICE_VALID=BigDecimal.TEN;
    public static final BigDecimal OFFER_PRICE_INVALID=BigDecimal.valueOf(-5);

    public static final String COMMENT_CONTENT ="All happen very good";
}
