package com.zorzanelli.repositoryinfo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScraperTest {

    @Test
    public void get_substring_based_on_reference_success() {

        Scraper scraper = new Scraper();
        String content = "<div class=\"text-mono f6 flex-auto pr-3 flex-order-2 flex-md-order-1 mt-2 mt-md-0\">\n" +
                "\n" +
                "      747 lines (690 sloc)\n" +
                "      <span class=\"file-info-divider\"></span>\n" +
                "    22.8 KB\n" +
                "  </div>";
        String reference = "text-mono f6 flex-auto pr-3 flex-order-2 flex-md-order-1 mt-2 mt-md-0";

        String result = scraper.getSubstringBasedOnReference(content, reference,"</span>", "</div>");

        assertEquals("22.8 KB", result, "The result must be equals: 22.8 KB");
    }

}