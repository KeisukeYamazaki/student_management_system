var inputData = [
    {
        "id": "1010",
        "text": "鶴見"
    },
    {
        "id": "1040",
        "text": "横浜翠嵐"
    },
    {
        "id": "1050",
        "text": "城郷"
    },
    {
        "id": "1060",
        "text": "港北"
    },
    {
        "id": "1070",
        "text": "新羽"
    },
    {
        "id": "1080",
        "text": "岸根"
    },
    {
        "id": "1090",
        "text": "横浜市立東"
    },
    {
        "id": "1100",
        "text": "新栄"
    },
    {
        "id": "1110",
        "text": "川和"
    },
    {
        "id": "1120",
        "text": "市ヶ尾"
    },
    {
        "id": "1130",
        "text": "霧が丘"
    },
    {
        "id": "1140",
        "text": "白山"
    },
    {
        "id": "1141",
        "text": "白山 美術"
    },
    {
        "id": "1160",
        "text": "荏田"
    },
    {
        "id": "1170",
        "text": "元石川"
    },
    {
        "id": "1180",
        "text": "希望ヶ丘"
    },
    {
        "id": "1190",
        "text": "旭"
    },
    {
        "id": "1220",
        "text": "松陽"
    },
    {
        "id": "1250",
        "text": "瀬谷"
    },
    {
        "id": "1260",
        "text": "瀬谷西"
    },
    {
        "id": "1270",
        "text": "横浜平沼"
    },
    {
        "id": "1280",
        "text": "光陵"
    },
    {
        "id": "1290",
        "text": "保土ヶ谷"
    },
    {
        "id": "1300",
        "text": "舞岡"
    },
    {
        "id": "1320",
        "text": "上矢部"
    },
    {
        "id": "1321",
        "text": "上矢部 美術"
    },
    {
        "id": "1330",
        "text": "金井"
    },
    {
        "id": "1350",
        "text": "横浜市立戸塚"
    },
    {
        "id": "1351",
        "text": "横浜市立戸塚 音楽"
    },
    {
        "id": "1360",
        "text": "横浜市立桜丘"
    },
    {
        "id": "1440",
        "text": "柏陽"
    },
    {
        "id": "1460",
        "text": "横浜市立南"
    },
    {
        "id": "1470",
        "text": "横浜緑ヶ丘"
    },
    {
        "id": "1540",
        "text": "横浜市立金沢"
    },
    {
        "id": "1580",
        "text": "住吉"
    },
    {
        "id": "1640",
        "text": "百合丘"
    },
    {
        "id": "1830",
        "text": "鎌倉"
    },
    {
        "id": "1840",
        "text": "七里ガ浜"
    },
    {
        "id": "1850",
        "text": "大船"
    },
    {
        "id": "1860",
        "text": "深沢"
    },
    {
        "id": "1870",
        "text": "湘南"
    },
    {
        "id": "1890",
        "text": "藤沢西"
    },
    {
        "id": "1930",
        "text": "湘南台"
    },
    {
        "id": "2200",
        "text": "厚木"
    },
    {
        "id": "2210",
        "text": "厚木東"
    },
    {
        "id": "2240",
        "text": "厚木西"
    },
    {
        "id": "2250",
        "text": "海老名"
    },
    {
        "id": "2260",
        "text": "有馬"
    },
    {
        "id": "2280",
        "text": "大和"
    },
    {
        "id": "2290",
        "text": "大和南"
    },
    {
        "id": "2300",
        "text": "大和西"
    },
    {
        "id": "2320",
        "text": "座間"
    },
    {
        "id": "2350",
        "text": "綾瀬"
    },
    {
        "id": "2360",
        "text": "綾瀬西"
    },
    {
        "id": "2380",
        "text": "上鶴間"
    },
    {
        "id": "2440",
        "text": "上溝"
    },
    {
        "id": "2530",
        "text": "相原 畜産科学"
    },
    {
        "id": "2531",
        "text": "相原 食品科学"
    },
    {
        "id": "2532",
        "text": "相原 環境緑地"
    },
    {
        "id": "2533",
        "text": "相原 総合ビジネス"
    },
    {
        "id": "2540",
        "text": "中央農業 園芸科学"
    },
    {
        "id": "2541",
        "text": "中央農業 畜産科学"
    },
    {
        "id": "2542",
        "text": "中央農業 農業総合"
    },
    {
        "id": "2550",
        "text": "神奈川工業 機械"
    },
    {
        "id": "2551",
        "text": "神奈川工業 建設"
    },
    {
        "id": "2552",
        "text": "神奈川工業 電気"
    },
    {
        "id": "2553",
        "text": "神奈川工業 デザイン"
    },
    {
        "id": "2560",
        "text": "商工 総合技術"
    },
    {
        "id": "2561",
        "text": "商工 総合ビジネス"
    },
    {
        "id": "2690",
        "text": "川崎市立川崎総合科学 情報工学"
    },
    {
        "id": "2691",
        "text": "川崎市立川崎総合科学 総合電気"
    },
    {
        "id": "2692",
        "text": "川崎市立川崎総合科学 電子機械"
    },
    {
        "id": "2693",
        "text": "川崎市立川崎総合科学 建設工学"
    },
    {
        "id": "2694",
        "text": "川崎市立 川崎総合科学 デザイン"
    },
    {
        "id": "2695",
        "text": "川崎市立川崎総合科学 科学"
    },
    {
        "id": "2740",
        "text": "横浜商業 商業"
    },
    {
        "id": "2741",
        "text": "横浜商業 国際"
    },
    {
        "id": "2742",
        "text": "横浜商業 スポーツマネジメント"
    },
    {
        "id": "2790",
        "text": "二俣川看護福祉 看護"
    },
    {
        "id": "2791",
        "text": "二俣川看護福祉 福祉"
    },
    {
        "id": "2810",
        "text": "神奈川総合 個性化"
    },
    {
        "id": "2811",
        "text": "神奈川総合 国際文化"
    },
    {
        "id": "2880",
        "text": "横浜市立みなと総合"
    },
    {
        "id": "2910",
        "text": "横浜桜陽"
    },
    {
        "id": "2930",
        "text": "藤沢工科"
    },
    {
        "id": "2950",
        "text": "横浜旭陵"
    },
    {
        "id": "2990",
        "text": "鶴見総合"
    },
    {
        "id": "3000",
        "text": "横浜清陵"
    },
    {
        "id": "3010",
        "text": "金沢総合"
    },
    {
        "id": "3030",
        "text": "藤沢総合"
    },
    {
        "id": "3040",
        "text": "神奈川総合産業"
    },
    {
        "id": "3060",
        "text": "横浜緑園"
    },
    {
        "id": "3090",
        "text": "横浜国際 国際"
    },
    {
        "id": "3091",
        "text": "横浜国際 国際バカロレア"
    },
    {
        "id": "3140",
        "text": "横浜栄"
    },
    {
        "id": "3160",
        "text": "座間総合"
    },
    {
        "id": "3170",
        "text": "横浜サイエンスフロンティア"
    },
    {
        "id": "3200",
        "text": "藤沢清流"
    },
    {
        "id": "4171",
        "text": "藤嶺学園藤沢 普通 併願"
    },
    {
        "id": "4172",
        "text": "藤嶺学園藤沢 普通 併願"
    },
    {
        "id": "4211",
        "text": "藤沢翔陵 文理 併願"
    },
    {
        "id": "4213",
        "text": "藤沢翔陵 特進 併願"
    },
    {
        "id": "4217",
        "text": "藤沢翔陵 商業 併願"
    },
    {
        "id": "5071",
        "text": "北鎌倉女子学園 普通 併願"
    },
    {
        "id": "5076",
        "text": "北鎌倉女子学園 特進 併願"
    },
    {
        "id": "5161",
        "text": "相模女子大学 進学 併願"
    },
    {
        "id": "5160",
        "text": "相模女子大学 進学 単願"
    },
    {
        "id": "5166",
        "text": "相模女子大学 特別進学 併願"
    },
    {
        "id": "5411",
        "text": "白鵬女子 セレクトα 併願"
    },
    {
        "id": "5611",
        "text": "白鵬女子 セレクトβ 併願"
    },
    {
        "id": "5429",
        "text": "白鵬女子 国際α 併願"
    },
    {
        "id": "5414",
        "text": "白鵬女子 国際β 併願"
    },
    {
        "id": "5416",
        "text": "白鵬女子 メディア表現α 併願"
    },
    {
        "id": "5420",
        "text": "白鵬女子 総合 併願"
    },
    {
        "id": "5423",
        "text": "白鵬女子 スポーツα 併願"
    },
    {
        "id": "5426",
        "text": "白鵬女子 保育 併願"
    },
    {
        "id": "5538",
        "text": "白鵬女子 フードコーディネート 併願"
    },
    {
        "id": "8131",
        "text": "横浜富士見丘学園 スタンダード 併願"
    },
    {
        "id": "6041",
        "text": "麻布大学附属 進学 併願"
    },
    {
        "id": "6045",
        "text": "麻布大学附属 特進 併願"
    },
    {
        "id": "8141",
        "text": "麻布大学附属 S特進 併願"
    },
    {
        "id": "6130",
        "text": "桜美林 進学 併願"
    },
    {
        "id": "8300",
        "text": "桜美林 国公立 併願"
    },
    {
        "id": "6135",
        "text": "桜美林 特別進学 併願"
    },
    {
        "id": "6171",
        "text": "柏木学園 スタンダード 併願"
    },
    {
        "id": "6174",
        "text": "柏木学園 アドバンス 併願"
    },
    {
        "id": "6177",
        "text": "柏木学園 情報 併願"
    },
    {
        "id": "6170",
        "text": "柏木学園 スタンダード 単願"
    },
    {
        "id": "6173",
        "text": "柏木学園 アドバンス 単願"
    },
    {
        "id": "6176",
        "text": "柏木学園 情報 単願"
    },
    {
        "id": "6249",
        "text": "鵠沼 英語 併願"
    },
    {
        "id": "6241",
        "text": "鵠沼 理数 併願"
    },
    {
        "id": "6245",
        "text": "鵠沼 文理 併願"
    },
    {
        "id": "6325",
        "text": "向上 特進 併願"
    },
    {
        "id": "6323",
        "text": "向上 選抜 併願"
    },
    {
        "id": "6321",
        "text": "向上 文理 併願"
    },
    {
        "id": "6334",
        "text": "光明学園相模原 文理 併願"
    },
    {
        "id": "6331",
        "text": "光明学園相模原 総合 併願"
    },
    {
        "id": "6498",
        "text": "湘南工科大学附属 進学アドバンス(セレクト) 併願"
    },
    {
        "id": "6494",
        "text": "湘南工科大学附属 進学アドバンス(一般) 併願"
    },
    {
        "id": "6491",
        "text": "湘南工科大学附属 進学スタンダード 併願"
    },
    {
        "id": "6502",
        "text": "湘南工科大学附属 技術 併願"
    },
    {
        "id": "6771",
        "text": "中央大学附属横浜 普通 併願"
    },
    {
        "id": "6795",
        "text": "鶴見大学附属 特進 併願"
    },
    {
        "id": "6791",
        "text": "鶴見大学附属 総合進学 併願"
    },
    {
        "id": "6825",
        "text": "桐蔭学園 プログレス 併願"
    },
    {
        "id": "6833",
        "text": "桐蔭学園 アドバンス 併願"
    },
    {
        "id": "6821",
        "text": "桐蔭学園 スタンダード 併願"
    },
    {
        "id": "6851",
        "text": "東海大学付属相模 普通 併願"
    },
    {
        "id": "6850",
        "text": "東海大学付属相模 普通 単願"
    },
    {
        "id": "7104",
        "text": "日本大学 特別進学 併願"
    },
    {
        "id": "7100",
        "text": "日本大学 総合進学 併願"
    },
    {
        "id": "7108",
        "text": "日本大学 ｽｰﾊﾟｰｸﾞﾛｰﾊﾞﾙ 併願"
    },
    {
        "id": "7120",
        "text": "日本大学第三 普通 推薦"
    },
    {
        "id": "7122",
        "text": "日本大学第三 特進 推薦"
    },
    {
        "id": "7141",
        "text": "日本大学藤沢 普通 併願"
    },
    {
        "id": "7140",
        "text": "日本大学藤沢 普通 単願"
    },
    {
        "id": "7255",
        "text": "平塚学園 特別進学 併願"
    },
    {
        "id": "7252",
        "text": "平塚学園 進学 併願"
    },
    {
        "id": "7251",
        "text": "平塚学園 文理 併願"
    },
    {
        "id": "7250",
        "text": "平塚学園 文理 単願"
    },
    {
        "id": "7640",
        "text": "法政大学第二 普通 単願"
    },
    {
        "id": "7410",
        "text": "山手学院 普通 併願"
    },
    {
        "id": "8127",
        "text": "横浜 プレミア 併願"
    },
    {
        "id": "8122",
        "text": "横浜 アドバンス 併願"
    },
    {
        "id": "8166",
        "text": "横浜 アクティブ 併願"
    },
    {
        "id": "7430",
        "text": "横浜学園 普通 併願"
    },
    {
        "id": "7431",
        "text": "横浜学園 クリエイティブ 併願"
    },
    {
        "id": "7445",
        "text": "横浜商科大学 特進 併願"
    },
    {
        "id": "7442",
        "text": "横浜商科大学 進学 併願"
    },
    {
        "id": "7448",
        "text": "横浜商科大学 商業 併願"
    },
    {
        "id": "7441",
        "text": "横浜商科大学 進学 単願"
    },
    {
        "id": "7444",
        "text": "横浜商科大学 特進 単願"
    },
    {
        "id": "7447",
        "text": "横浜商科大学 商業 単願"
    },
    {
        "id": "7464",
        "text": "横浜翠陵 特進 併願"
    },
    {
        "id": "7467",
        "text": "横浜翠陵 国際 併願"
    },
    {
        "id": "7461",
        "text": "横浜翠陵 文理 併願"
    },
    {
        "id": "7474",
        "text": "横浜清風 特進 併願"
    },
    {
        "id": "7471",
        "text": "横浜清風 総合進学 併願"
    },
    {
        "id": "7487",
        "text": "横浜創英 特進 併願"
    },
    {
        "id": "7484",
        "text": "横浜創英 文理 併願"
    },
    {
        "id": "7481",
        "text": "横浜創英 普通 併願"
    },
    {
        "id": "7483",
        "text": "横浜創英 文理 単願"
    },
    {
        "id": "7486",
        "text": "横浜創英 特進 単願"
    },
    {
        "id": "7499",
        "text": "横浜創学館 特別進学 併願"
    },
    {
        "id": "7497",
        "text": "横浜創学館 文理選抜 併願"
    },
    {
        "id": "7491",
        "text": "横浜創学館 総合進学(一般) 併願"
    },
    {
        "id": "7513",
        "text": "横浜隼人 特別選抜 併願"
    },
    {
        "id": "7517",
        "text": "横浜隼人 特進 併願"
    },
    {
        "id": "7511",
        "text": "横浜隼人 普通 併願"
    },
    {
        "id": "7516",
        "text": "横浜隼人 国際語 併願"
    },
    {
        "id": "7530",
        "text": "和光 併願"
    },
    {
        "id": "7532",
        "text": "和光 推薦"
    }
];

$(document).ready(function() {
    var firstchoice = $('#showfirstChoice').attr("value");
    var firstchoiceName;
    for(var i in inputData) {
        if(firstchoice == inputData[i].id) {
            firstchoiceName = inputData[i].text;
            break;
        }
    }
    $('.showfirstChoice').text(firstchoiceName);

    var secondchoice = $('#showsecondChoice').attr("value");
    var secondchoiceName;
    for(var i in inputData) {
        if(secondchoice == inputData[i].id) {
            secondchoiceName = inputData[i].text;
            break;
        }
    }
    $('.showsecondChoice').text(secondchoiceName);

    var thirdchoice = $('#showthirdChoice').attr("value");
    var thirdchoiceName;
    for(var i in inputData) {
        if(thirdchoice == inputData[i].id) {
            thirdchoiceName = inputData[i].text;
            break;
        }
    }
    $('.showthirdChoice').text(thirdchoiceName);

    var public1 = $('#showpublicSchool1').attr("value");
    var public1Name;
    for(var i in inputData) {
        if(public1 == inputData[i].id) {
            public1Name = inputData[i].text;
            break;
        }
    }
    $('.showpublicSchool1').text(public1Name);

    var public2 = $('#showpublicSchool2').attr("value");
    var public2Name;
    for(var i in inputData) {
        if(public2 == inputData[i].id) {
            public2Name = inputData[i].text;
            break;
        }
    }
    $('.showpublicSchool2').text(public2Name);

    var public3 = $('#showpublicSchool3').attr("value");
    var public3Name;
    for(var i in inputData) {
        if(public3 == inputData[i].id) {
            public3Name = inputData[i].text;
            break;
        }
    }
    $('.showpublicSchool3').text(public3Name);

    var private1 = $('#showprivateSchool1').attr("value");
    var private1Name;
    for(var i in inputData) {
        if(private1 == inputData[i].id) {
            private1Name = inputData[i].text;
            break;
        }
    }
    $('.showprivateSchool1').text(private1Name);

    var private2 = $('#showprivateSchool2').attr("value");
    var private2Name;
    for(var i in inputData) {
        if(private2 == inputData[i].id) {
            private2Name = inputData[i].text;
            break;
        }
    }
    $('.showprivateSchool2').text(private2Name);

    var private3 = $('#showprivateSchool3').attr("value");
    var private3Name;
    for(var i in inputData) {
        if(private3 == inputData[i].id) {
            private3Name = inputData[i].text;
            break;
        }
    }
    $('.showprivateSchool3').text(private3Name);
});