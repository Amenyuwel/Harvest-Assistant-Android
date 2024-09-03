package com.example.app;

import java.util.HashMap;
import java.util.Map;

public class GrowthStageData {
    public static class GrowthStage {
        String stage;
        String description;
        String suggestions;

        public GrowthStage(String stage, String description, String suggestions) {
            this.stage = stage;
            this.description = description;
            this.suggestions = suggestions;
        }
    }

    private static final Map<String, GrowthStage> growthStages = new HashMap<>();

    static {
        // Rice stages
        growthStages.put("Rice-1", new GrowthStage("Pag-usbong",
                "Kini mao ang sugod diin ang binhi sa humay nag-absorb og tubig ug nagsugod og pagtubo. Una, magsugod ang gamut (radikulo), ug sunod niini magsugod ang sanga.",
                "•Seed Selection: Gamita ang de-kalidad, walay sakit nga mga binhi nga adunay taas nga rate sa pagtubo.\n" +
                        "•Seed Treatment: Trataron ang mga binhi gamit ang fungicide o biofertilizer aron mapanalipdan batok sa mga pathogen.\n" +
                        "•Water Management: Siguroha ang igo apan dili sobra nga kaumog alang sa optimal nga pagtubo sa binhi."));
        growthStages.put("Rice-2", new GrowthStage("Paglaki sa Binhi",
                "Niining yugto, ang halaman sa palay nagapamunga og dugang mga sanga gikan sa base sa pangunahing tangkay. Tawag niini nga “tillers” ang mga sanga, ug kini mahimong produktibong mga sanga. Ang bilang sa tillers mahimong maglainon base sa mga faktor sama sa klase sa palay, pagkabutang sa tanom, ug kahandaan sa sustansya.",
                "•Nursery Management: Gamita ang maayong pagkaandam nga higdaanan sa nursery nga adunay husto nga drainage ug pagkamabungahon.\n" +
                        "•Transplanting Timing:  Ibalhin ang mga semilya sa husto nga edad (kasagaran 20-30 ka adlaw ang edad) aron maminusan ang shock sa pagbalhin.\n" +
                        "•Spacing: Tumanon ang angay nga gibug-aton sa tanom aron malikayan ang sobra nga pagdagsang ug masiguro ang maayong sirkulasyon sa hangin."));
        growthStages.put("Rice-3", new GrowthStage("Pagtubo sa Mga Sanga",
                "In this stage, the rice plant begins to produce additional shoots from the base of the main stem. These shoots are called tillers and eventually develop into productive stems. The number of tillers can vary depending on factors such as variety, plant spacing, and nutrient availability.",
                "•Nutrient Management: I-apply ang nitrogen fertilizers (urea) sa split doses aron mahikay ang pagtubo sa sanga.\n" +
                        "•Management: Padayonon ang mabaw nga pagbaha aron mapalambo ang himsog nga paglambo sa sanga.\n" +
                        "•Weed Control: Ipahigayon ang epektibo nga pagdumala sa sagbot aron maminusan ang kompetisyon alang sa mga nutrisyon."));
        growthStages.put("Rice-4", new GrowthStage("Pag-elongasyon sa Tangkay",
                " Ang pangunahing tangkay ug mga sanga nagahinay og pagdako paingon sa itaas. Ang halaman sa palay nagakuha og karakteristikang matarong nga hitsura samtang nagpangita sa kahayag sa adlaw.",
                "•Nutrient Boost: I-apply ang balanse nga dosis sa mga fertilizers, lakip na ang nitrogen, phosphorus, ug potassium.\n" +
                        "•Pest Management: Bantayan ug kontrolon ang mga peste sama sa mga stem borers ug leafhoppers nga makadaot sa nagtubo nga tanom.\n" +
                        "•Water Management: Sigurohon ang padayon nga suplay sa tubig nga walay pagbaha."));
        growthStages.put("Rice-5", new GrowthStage("Simula sa Panikulo",
                "Ang enerhiya nagasentro na sa reproductive growth. Nagahimo na ang mga panikulo, nga naglakip sa mga bulaklak sa palay, sa mga punta sa pangunahing tangkay ug mga sanga.",
                "•Nutrient Application: I-apply ang katapusang dosis sa nitrogen fertilizer aron suportahan ang pagpalambo sa pamiyuos.\n" +
                        "•Disease Control: Gamiton ang mga fungicide o mga barayti nga makasukol sa mga sakit sama sa rice blast ug sheath blight.\n" +
                        "•Water Management: Padayonon ang optimal nga lebel sa tubig aron suportahan ang pagtubo sa reproduktibo."));
        growthStages.put("Rice-6", new GrowthStage("Yugto sa pag-laki",
                "Ang mga panikulo kompleto nang magsugod og mubunga ang mga bulaklak sa palay. Matag bulaklak adunay stamen (lalaki nga bahin sa reproductive organ) ug pistil (babaye nga bahin sa reproductive organ).",
                "•Micro-nutrient Supply: Sigurohon ang pagkaanaa sa hinungdanon nga mga micronutrients sama sa zinc ug boron aron suportahan ang pagpamulak.\n" +
                        "•Pest Control: Panalipdan batok sa mga peste sama sa panicle mites ug mga insekto nga mokaon sa lugas.\n" +
                        "•Water Management: Padayonon ang pagbaha sa uma apan likayan ang lawom nga tubig nga makalumos sa mga pamiyuos."));
        growthStages.put("Rice-7", new GrowthStage("Paglalabas sa Bulaklak",
                "Sa kinaadman, ang palay nag-aani sa iyang kaugalingon, diin ang polen gikan sa stamen nagpabunga sa pistil sulod sa parehong bulaklak. Apan mahitabo usab ang cross-pollination.",
                "•Pollination: Sigurohon ang maayong sirkulasyon sa hangin aron mapadali ang polinasyon, ilabi na sa mga hybrid nga barayti.\n" +
                        "•Temperature Management: Panalipdan ang mga tanom gikan sa sobra nga temperatura nga makaapekto sa pagpamulak ug polinasyon.\n" +
                        "•Pest Management: Kontrolon ang mga peste nga makadaot sa mga bulak, sama sa stink bugs ug leafhoppers."));
        growthStages.put("Rice-8", new GrowthStage("Pagpuno sa Butil",
                "Human sa polinasyon, ang nabuong bulaklak nag-umang sa butil. Niining yugto, ang mga butil nagapuno og starch ug uban pang sustansya, busa nagdako sila.",
                "•Nutrient Support: I-apply ang potassium aron mapalambo ang pagpuno sa lugas ug kinatibuk-ang ani.\n" +
                        "•Water Management: Padayonon ang optimal nga lebel sa tubig aron suportahan ang pagpuno sa lugas, nga hinay-hinay nga ginaminusan samtang ang tanom nagkaduol sa pagkahinog.\n" +
                        "•Disease Control: Bantayan ug pagdumala sa mga sakit sama sa bacterial leaf blight nga makaapekto sa kalidad sa lugas."));
        growthStages.put("Rice-9", new GrowthStage("Pagkamature ug Pagkapula",
                "Padayon nga nagdako ug nagkapula ang mga butil. Ang halaman sa palay unti-unting nagakabukog ug pagkahuman sa pagkapula, magsugod na ang pagtigulang (senescence), ug ang mga tangkay ug dahon nagasugod og pagkuyaw.",
                "•Water Management: Hinay-hinay nga pagkunhod sa suplay sa tubig aron mapadali ang pagpauga ug maminusan ang risgo sa pagkahulog.\n" +
                        "•Pest Control: Panalipdan ang hinog nga mga lugas gikan sa mga peste sama sa mga langgam ug ilaga.\n" +
                        "•Harvest Timing: Anihon sa husto nga panahon kung ang mga lugas hingpit nga hinog apan dili sobra aron malikayan ang mga pagkawala."));
        growthStages.put("Rice-10", new GrowthStage("Pag-aani",
                "Sa pagkabutang sa mga butil, handa na silang anihon.",
                "•Proper Timing: Anihon kung ang 80-90% sa mga lugas hinog na aron masiguro ang maximum nga ani ug kalidad.\n" +
                        "•Harvesting Method: Gamiton ang angay nga mga pamaagi sa pag-ani aron maminusan ang pagkawala ug kadaot sa lugas.\n" +
                        "•Post-Harvest Handling: Husto nga pagpauga sa mga lugas aron maminusan ang sulod nga kaumog ngadto sa luwas nga lebel alang sa pagtipig (mga 12-14%)."));
        // Corn stages
        growthStages.put("Corn-1", new GrowthStage("Pagtubo",
                "Kini mao ang yugto diin ang buto sa mais nag-absorb og tubig gikan sa yuta ug nagsugod og pagtubo. Ang radical (gamut) mauna nga magsugod, susundan sa coleoptile (sanga).",
                "•Seed Quality: Gamita ang taas og kalidad, walay sakit nga mga binhi nga adunay taas nga rate sa pagtubo.\n" +
                        "•Seed Treatment: Trataron ang mga binhi gamit ang fungicide o insecticide aron mapanalipdan batok sa mga sakit ug peste nga gikan sa yuta.\n" +
                        "•Optimal Planting Conditions: Siguroha nga ang yuta adunay husto nga kaumog, temperatura (ideally 10-30°C), ug giladmon sa pagtanum (mga 1.5 hangtod 2 pulgada) para sa maayong pagtubo."));

        growthStages.put("Corn-2", new GrowthStage("Yugto sa Pagpananom",
                "Sa yugto nga kini, ang tanom sa mais padayon nga naglambo sa iyang mga gamut ug sistema sa sanga. Ang unang mga dahon, nga nailhan nga coleoptile leaves, nag-abli ug nagsugod ang photosynthesis.",
                "•Soil Preparation: Siguruhon ang maayong andam sa yuta nga adunay maayong istruktura ug drainage.\n" +
                        "•Early Nutrient Management: I-apply ang starter fertilizers nga adunay daghang posporus aron suportahan ang pagtubo sa mga gamut.\n" +
                        "•Weed Control: Implementar ang early weed control aron ma-reduce ang kompetisyon alang sa nutrisyon, tubig, ug kahayag."));

        growthStages.put("Corn-3", new GrowthStage("V6 Stage (Anim nga Dahon sa Leeg",
                "Niining yugto, ang tanom sa mais adunay unom ka bug-os nga mga dahon nga mibuto na. Kini usa ka kritikal nga yugto alang sa pag-angkon sa nutrisyon ug paglambo sa una."
                ,"•Nutrient Application: Tabangan sa abono nga nitrogen aron suportahan ang paspas nga pagtubo ug paglambo.\n" +
                "•Pest and Disease Management: Bantayan ug kontrolon ang mga peste sama sa cutworms ug mga sakit sama sa seedling blight.\n" +
                "•Water Management: Sigurohon ang padayon nga lebel sa kaumog aron malikayan ang stress."));

        growthStages.put("Corn-4", new GrowthStage("V12 Stage (Dose nga Dahon sa Leeg",
                "Sa kini nga yugto, ang tanom sa mais adunay dose ka bug-os nga mga dahon nga mibuto na. Kini padayon nga naglambo og dali, ug ang paglambo sa mga gamut importante alang sa pag-angkon og kusog sa pagtukod ug pag-angkon sa nutrisyon.",
                "•Nutrient Management: Padayon ang pag-aplikar sa nitrogen kung kinahanglan ug hunahunaa ang foliar feeding nga adunay micronutrients sama sa zinc.\n" +
                        "•Irrigation: Hatagan og igong tubig, ilabi na sa panahon sa hulaw, aron suportahan ang kusog nga pagtubo.\n" +
                        "•Pest Control: Pangitaon ang mga peste sama sa corn borers ug himoon ang angay nga mga lakang sa pagkontrol."));

        growthStages.put("Corn-5", new GrowthStage("V18 Stage (Onse nga Dahon sa Leeg",
                "Niining punto, ang tanom sa mais adunay onse ka bug-os nga mga dahon nga mibuto na. Kini nagkaduol na sa katapusan sa yugto sa paglambo ug nag-andam na alang sa reproduktibong paglambo.",
                "•Nutrient Support: Padayon nga balanse ang suplay sa nutrisyon, sigurohon ang pagkaanaa sa potassium ug phosphorus.\n" +
                        "•Disease Monitoring: Bantayan ang mga sakit sa dahon sama sa northern corn leaf blight ug i-aplikar ang fungicide kung kinahanglan.\n" +
                        "•Water Management: Padayon nga hatagan og igong tubig, likayan ang stress sa tubig."));

        growthStages.put("Corn-6", new GrowthStage("Pag-usbaw sa Tassel",
                "Kini nagtandog sa simula sa reproduktibong yugto. Ang tassel, nga naglakip sa mga anther nga nag-produce og pollen, mibuto gikan sa itaas sa tanom.",
                "•Pest and Disease Control: Panalipdan ang tanom gikan sa mga peste sa tassel ug silk sama sa corn earworms.\n" +
                        "•Water Management: Sigurohon ang igong suplay sa tubig samtang ang tanom nangandam alang sa reproduksyon.\n" +
                        "•Nutrient Management: Sigurohon nga adunay igong nitrogen ug uban pang sustansya ang tanom."));

        growthStages.put("Corn-7", new GrowthStage("Paglabay sa Silk",
                "Sayo human sa pag-usbaw sa tassel, ang silk mibuto gikan sa mga ear shoot nodes. Ang matag silk nagrepresentar og potensyal nga kernel, ug kini nagkapkap og pollen alang sa pagpabunga.",
                "•Pollination: Sigurohon ang maayong daloy sa hangin ug kondisyon sa palibot aron mapadali ang polinasyon.\n" +
                        "•Pest Control: Kontrolon ang mga peste sama sa Japanese beetles nga makadaot sa silk ug makaapekto sa polinasyon.\n" +
                        "•Water Management: Mantenahon ang optimal nga kaumog sa yuta aron suportahan ang paglambo sa silk ug tassel."));

        growthStages.put("Corn-8", new GrowthStage("Polinasyon",
                "Ang pollen nga gikan sa tassel nagkabug-at sa mga silk, nga nagdala sa pagpabunga sa mga ovules. Ang matag polinasyon importante alang sa paglambo sa mga kernel.",
                "•Environmental Management: Likayan ang mga kondisyon sa stress (hulaw, taas nga temperatura) panahon sa polinasyon aron masiguro ang pagbutang sa kernel.\n" +
                        "•Pest Control: Padayon nga bantayan ug kontrolon ang mga peste nga makabalda sa polinasyon.\n" +
                        "•Irrigation: Sigurohon ang padayon nga kaumog sa yuta aron suportahan ang malampuson nga polinasyon ug paglambo sa kernel."));

        growthStages.put("Corn-9", new GrowthStage("Pagpuno sa Lug",
                "Ang tanom sa mais nag-abot sa physiological maturity human ang mga kernel nakab-ot na ang ilang maximum nga dry weight ug ang kantidad sa kahumon nagkunhod ngadto sa optimal nga lebel alang sa pag-ani.",
                "•Nutrient Application: Ibutang ang potassium aron suportahan ang pagpuno sa kernel ug kinatibuk-ang kalidad sa lugas.\n" +
                        "•Water Management: Hatagan og igong tubig aron malikayan ang stress ug masiguro ang husto nga pagpuno sa lugas.\n" +
                        "•Disease Monitoring: Bantayan ang mga sakit sa dalunggan ug punoan ug pagdumala sumala niana."));

        growthStages.put("Corn-10", new GrowthStage("Pagkahinog",
                "Sang tanom sa mais nag-abot sa physiological maturity human ang mga kernel nakab-ot na ang ilang maximum nga dry weight ug ang kantidad sa kahumon nagkunhod ngadto sa optimal nga lebel alang sa pag-ani.",
                "•Water Management: Hinay-hinay nga maminusan ang irigasyon samtang ang tanom nagkaduol sa pagkahamtong aron mapadali ang pag-uga.\n" +
                        "•Pest Control: Panalipdan ang nagkahamtong nga mga dalunggan gikan sa mga peste sama sa mga langgam ug ilaga.\n" +
                        "•Disease Control: Bantayan ang mga sakit sa ulahi nga panahon ug himoon ang kinahanglan nga mga lakang aron panalipdan ang tanom."));

        growthStages.put("Corn-11", new GrowthStage("Pag-ani",
                "Human nga nahinog ang mais, kini andam na sa pag-ani. Ang mga pamaagi sa pag-ani naglain-lain apan kadalasan naglakip og mekanikal nga kagamitan aron kuhaon ang mga ear gikan sa mga sanga.",
                "•Timing: Anihon sa husto nga panahon kung ang mga kernel nakab-ot na ang optimal nga kaumog (mga 20-25% alang sa mekanikal nga pag-ani).\n" +
                        "•Harvesting Method: Gamiton ang angay nga kagamitan ug teknik sa pag-ani aron maminusan ang pagkawala ug kadaot sa lugas.\n" +
                        "•Post-Harvest Handling: Ugaon ug tipigan ang lugas sa husto aron malikayan ang pagkadaot ug mapadayon ang kalidad."));
        // Add more crops and stages as needed
    }

    public static GrowthStage getGrowthStage(String crop, int week) {
        return growthStages.getOrDefault(crop + "-" + week,
                new GrowthStage("Unknown Stage", "No information available for this stage.", "Consult an agronomist."));
    }
}