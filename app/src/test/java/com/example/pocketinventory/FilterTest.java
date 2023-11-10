package com.example.pocketinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FilterTest {
    //There are not too much to test. Few functions can run without UI.
    @Test
    public void testParseDate() {
        Date date = ItemFilterFragment.parseDate("01/01/1970");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(sdf.format(date), "01/01/1970");
        Date date2 = ItemFilterFragment.parseDate("");
        assertNull(date2);
        Date date3 = ItemFilterFragment.parseDate(null);
        assertNull(date2);
    }

    @Test (expected = Exception.class)
    public void itemTest() {

        Date date = ItemFilterFragment.parseDate("01/01/1970");
        ArrayList<String> tags = new ArrayList<String>();
        Item item = new Item(date, "make", "model", "description", 0.0, "comment", "serialNumber", tags);
    }
}
