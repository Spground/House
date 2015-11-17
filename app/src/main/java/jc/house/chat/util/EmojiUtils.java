/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jc.house.chat.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import jc.house.R;

public class EmojiUtils {
	public static final String ee_0 = "[:0a:]";
	public static final String ee_1 = "[:1b:]";
	public static final String ee_2 = "[:2c:]";
	public static final String ee_3 = "[:3d:]";
	public static final String ee_4 = "[:4e:]";
	public static final String ee_5 = "[:5f:]";
	public static final String ee_6 = "[:6g:]";
	public static final String ee_7 = "[:7h:]";
	public static final String ee_8 = "[:8i:]";
	public static final String ee_9 = "[:9j:]";
	public static final String ee_10 = "[:10k:]";
	public static final String ee_11 = "[:11l:]";
	public static final String ee_12 = "[:12m:]";
	public static final String ee_13 = "[:13n:]";
	public static final String ee_14 = "[:14o:]";
	public static final String ee_15 = "[:15p:]";
	public static final String ee_16 = "[:16q:]";
	public static final String ee_17 = "[:17r:]";
	public static final String ee_18 = "[:18s:]";
	public static final String ee_19 = "[:19t:]";
	public static final String ee_20 = "[:20u:]";
	public static final String ee_21 = "[:21v:]";
	public static final String ee_22 = "[:22w:]";
	public static final String ee_23 = "[:23x:]";
	public static final String ee_24 = "[:24y:]";
	public static final String ee_25 = "[:25z:]";
	public static final String ee_26 = "[:26a:]";
	public static final String ee_27 = "[:27b:]";
	public static final String ee_28 = "[:28c:]";
	public static final String ee_29 = "[:29d:]";
	public static final String ee_30 = "[:30e:]";
	public static final String ee_31 = "[:31f:]";
	public static final String ee_32 = "[:32g:]";
	public static final String ee_33 = "[:33h:]";
	public static final String ee_34 = "[:34i:]";
	public static final String ee_35 = "[:35j:]";
	public static final String ee_36 = "[:36k:]";
	public static final String ee_37 = "[:37l:]";
	public static final String ee_38 = "[:38m:]";
	public static final String ee_39 = "[:39n:]";
	public static final String ee_40 = "[:40o:]";
	public static final String ee_41 = "[:41p:]";
	public static final String ee_42 = "[:42q:]";
	public static final String ee_43 = "[:43r:]";
	public static final String ee_44 = "[:44s:]";
	public static final String ee_45 = "[:45t:]";
	public static final String ee_46 = "[:46u:]";
	public static final String ee_47 = "[:47v:]";
	public static final String ee_48 = "[:48w:]";
	public static final String ee_49 = "[:49x:]";
	public static final String ee_50 = "[:50y:]";
	public static final String ee_51 = "[:51z:]";
	public static final String ee_52 = "[:52a:]";
	public static final String ee_53 = "[:53b:]";
	public static final String ee_54 = "[:54c:]";
	public static final String ee_55 = "[:55d:]";
	public static final String ee_56 = "[:56e:]";
	public static final String ee_57 = "[:57f:]";
	public static final String ee_58 = "[:58g:]";
	public static final String ee_59 = "[:59h:]";
	public static final String ee_60 = "[:60i:]";
	public static final String ee_61 = "[:61j:]";
	public static final String ee_62 = "[:62k:]";
	public static final String ee_63 = "[:63l:]";
	public static final String ee_64 = "[:64m:]";
	public static final String ee_65 = "[:65n:]";
	public static final String ee_66 = "[:66o:]";
	public static final String ee_67 = "[:67p:]";
	public static final String ee_68 = "[:68q:]";
	public static final String ee_69 = "[:69r:]";
	public static final String ee_70 = "[:70s:]";
	public static final String ee_71 = "[:71t:]";
	public static final String ee_72 = "[:72u:]";
	public static final String ee_73 = "[:73v:]";
	public static final String ee_74 = "[:74w:]";
	public static final String ee_75 = "[:75x:]";
	public static final String ee_76 = "[:76y:]";
	public static final String ee_77 = "[:77z:]";
	public static final String ee_78 = "[:78a:]";
	public static final String ee_79 = "[:79b:]";
	public static final String ee_80 = "[:80c:]";
	public static final String ee_81 = "[:81d:]";
	public static final String ee_82 = "[:82e:]";
	public static final String ee_83 = "[:83f:]";
	public static final String ee_84 = "[:84g:]";
	public static final String ee_85 = "[:85h:]";
	public static final String ee_86 = "[:86i:]";
	public static final String ee_87 = "[:87j:]";
	public static final String ee_88 = "[:88k:]";
	public static final String ee_89 = "[:89l:]";
	public static final String ee_90 = "[:90m:]";
	public static final String ee_91 = "[:91n:]";
	private static final Factory spannableFactory = Factory
	        .getInstance();
	
	private static final Map<Pattern, Integer> emoticons = new HashMap<>();
	
	private static int simlesSize = 0;

	static {
	    addPattern(emoticons, ee_1, R.drawable.ee_1);
	    addPattern(emoticons, ee_2, R.drawable.ee_2);
	    addPattern(emoticons, ee_3, R.drawable.ee_3);
	    addPattern(emoticons, ee_4, R.drawable.ee_4);
	    addPattern(emoticons, ee_5, R.drawable.ee_5);
	    addPattern(emoticons, ee_6, R.drawable.ee_6);
	    addPattern(emoticons, ee_7, R.drawable.ee_7);
	    addPattern(emoticons, ee_8, R.drawable.ee_8);
	    addPattern(emoticons, ee_9, R.drawable.ee_9);
	    addPattern(emoticons, ee_10, R.drawable.ee_10);
	    addPattern(emoticons, ee_11, R.drawable.ee_11);
	    addPattern(emoticons, ee_12, R.drawable.ee_12);
	    addPattern(emoticons, ee_13, R.drawable.ee_13);
	    addPattern(emoticons, ee_14, R.drawable.ee_14);
	    addPattern(emoticons, ee_15, R.drawable.ee_15);
	    addPattern(emoticons, ee_16, R.drawable.ee_16);
	    addPattern(emoticons, ee_17, R.drawable.ee_17);
	    addPattern(emoticons, ee_18, R.drawable.ee_18);
	    addPattern(emoticons, ee_19, R.drawable.ee_19);
	    addPattern(emoticons, ee_20, R.drawable.ee_20);
	    addPattern(emoticons, ee_21, R.drawable.ee_21);
	    addPattern(emoticons, ee_22, R.drawable.ee_22);
	    addPattern(emoticons, ee_23, R.drawable.ee_23);
	    addPattern(emoticons, ee_24, R.drawable.ee_24);
	    addPattern(emoticons, ee_25, R.drawable.ee_25);
	    addPattern(emoticons, ee_26, R.drawable.ee_26);
	    addPattern(emoticons, ee_27, R.drawable.ee_27);
	    addPattern(emoticons, ee_28, R.drawable.ee_28);
	    addPattern(emoticons, ee_29, R.drawable.ee_29);
	    addPattern(emoticons, ee_30, R.drawable.ee_30);
	    addPattern(emoticons, ee_31, R.drawable.ee_31);
	    addPattern(emoticons, ee_32, R.drawable.ee_32);
	    addPattern(emoticons, ee_33, R.drawable.ee_33);
	    addPattern(emoticons, ee_34, R.drawable.ee_34);
	    addPattern(emoticons, ee_35, R.drawable.ee_35);
		addPattern(emoticons, ee_36, R.drawable.ee_36);
		addPattern(emoticons, ee_37, R.drawable.ee_37);
		addPattern(emoticons, ee_38, R.drawable.ee_38);
		addPattern(emoticons, ee_39, R.drawable.ee_39);
		addPattern(emoticons, ee_40, R.drawable.ee_40);
		addPattern(emoticons, ee_41, R.drawable.ee_41);
		addPattern(emoticons, ee_42, R.drawable.ee_42);
		addPattern(emoticons, ee_43, R.drawable.ee_43);
		addPattern(emoticons, ee_44, R.drawable.ee_44);
		addPattern(emoticons, ee_45, R.drawable.ee_45);
		addPattern(emoticons, ee_46, R.drawable.ee_46);
		addPattern(emoticons, ee_47, R.drawable.ee_47);
		addPattern(emoticons, ee_48, R.drawable.ee_48);
		addPattern(emoticons, ee_49, R.drawable.ee_49);
		addPattern(emoticons, ee_50, R.drawable.ee_50);
		addPattern(emoticons, ee_51, R.drawable.ee_51);
		addPattern(emoticons, ee_52, R.drawable.ee_52);
		addPattern(emoticons, ee_53, R.drawable.ee_53);
		addPattern(emoticons, ee_54, R.drawable.ee_54);
		addPattern(emoticons, ee_55, R.drawable.ee_55);
		addPattern(emoticons, ee_56, R.drawable.ee_56);
		addPattern(emoticons, ee_57, R.drawable.ee_57);
		addPattern(emoticons, ee_58, R.drawable.ee_58);
		addPattern(emoticons, ee_59, R.drawable.ee_59);
		addPattern(emoticons, ee_60, R.drawable.ee_60);
		addPattern(emoticons, ee_61, R.drawable.ee_61);
		addPattern(emoticons, ee_62, R.drawable.ee_62);
		addPattern(emoticons, ee_63, R.drawable.ee_63);
		addPattern(emoticons, ee_64, R.drawable.ee_64);
		addPattern(emoticons, ee_65, R.drawable.ee_65);
		addPattern(emoticons, ee_66, R.drawable.ee_66);
		addPattern(emoticons, ee_67, R.drawable.ee_67);
		addPattern(emoticons, ee_68, R.drawable.ee_68);
		addPattern(emoticons, ee_69, R.drawable.ee_69);
		addPattern(emoticons, ee_70, R.drawable.ee_70);
		addPattern(emoticons, ee_71, R.drawable.ee_71);
		addPattern(emoticons, ee_72, R.drawable.ee_72);
		addPattern(emoticons, ee_73, R.drawable.ee_73);
		addPattern(emoticons, ee_74, R.drawable.ee_74);
		addPattern(emoticons, ee_75, R.drawable.ee_75);
		addPattern(emoticons, ee_76, R.drawable.ee_76);
		addPattern(emoticons, ee_77, R.drawable.ee_77);
		addPattern(emoticons, ee_78, R.drawable.ee_78);
		addPattern(emoticons, ee_79, R.drawable.ee_79);
		addPattern(emoticons, ee_80, R.drawable.ee_80);
		addPattern(emoticons, ee_81, R.drawable.ee_81);
		addPattern(emoticons, ee_82, R.drawable.ee_82);
		addPattern(emoticons, ee_83, R.drawable.ee_83);
		addPattern(emoticons, ee_84, R.drawable.ee_84);
		addPattern(emoticons, ee_85, R.drawable.ee_85);
		addPattern(emoticons, ee_86, R.drawable.ee_86);
		addPattern(emoticons, ee_87, R.drawable.ee_87);
		addPattern(emoticons, ee_88, R.drawable.ee_88);
		addPattern(emoticons, ee_89, R.drawable.ee_89);
		addPattern(emoticons, ee_90, R.drawable.ee_90);
		addPattern(emoticons, ee_91, R.drawable.ee_91);
	    simlesSize = emoticons.size();
	}

	private static void addPattern(Map<Pattern, Integer> map, String smile,
	        int resource) {
	    map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	/**
	 * replace existing spannable with smiles
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable) {
	    boolean hasChanges = false;
	    for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(),
	                    matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                spannable.setSpan(new ImageSpan(context, entry.getValue()),
	                        matcher.start(), matcher.end(),
	                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }
	        }
	    }
	    return hasChanges;
	}

	public static Spannable getSmiledText(Context context, CharSequence text) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmiles(context, spannable);
	    return spannable;
	}
	
	public static boolean containsKey(String key){
		boolean b = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(key);
	        if (matcher.find()) {
	        	b = true;
	        	break;
	        }
		}
		
		return b;
	}
	
	public static int getSmilesSize(){
        return simlesSize;
    }
    
	
}
