using sfl.Data;
using sfl.Models;
using System;
using System.Linq;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;

namespace sfl.Data
{
    public static class DbInitializer
    {
        public static void Initialize(CompanyContext context)
        {
            context.Database.EnsureCreated();

            // Add staff roles.
            if (!context.StaffRoles.Any())
            {
                var staffRoles = new StaffRole[]
                {
                new StaffRole{ID=1,Name="Administrator"},
                new StaffRole{ID=2,Name="Warehouse manager"},
                new StaffRole{ID=3,Name="Warehouse worker"},
                new StaffRole{ID=4,Name="Logistics agent"},
                new StaffRole{ID=5,Name="Delivery driver"},
                };

                context.StaffRoles.AddRange(staffRoles);
                context.SaveChanges();
            }

            // Add cities.
            if (!context.Cities.Any())
            {
                var cities = new City[]
                {
                    new City {Code="1000",Name="Ljubljana - dostava"},
                    new City {Code="1001",Name="Ljubljana - poštni predali"},
                    new City {Code="1210",Name="Ljubljana Šentvid"},
                    new City {Code="1211",Name="Ljubljana-Šmartno"},
                    new City {Code="1215",Name="Medvode"},
                    new City {Code="1216",Name="Smlednik"},
                    new City {Code="1217",Name="Vodice"},
                    new City {Code="1218",Name="Komenda"},
                    new City {Code="1219",Name="Laze v Tuhinju"},
                    new City {Code="1221",Name="Motnik"},
                    new City {Code="1222",Name="Trojane"},
                    new City {Code="1223",Name="Blagovica"},
                    new City {Code="1225",Name="Lukovica"},
                    new City {Code="1230",Name="Domžale"},
                    new City {Code="1231",Name="Ljubljana Črnuče"},
                    new City {Code="1233",Name="Dob"},
                    new City {Code="1234",Name="Mengeš"},
                    new City {Code="1235",Name="Radomlje"},
                    new City {Code="1236",Name="Trzin"},
                    new City {Code="1241",Name="Kamnik"},
                    new City {Code="1242",Name="Stahovica"},
                    new City {Code="1251",Name="Moravče"},
                    new City {Code="1252",Name="Vače"},
                    new City {Code="1260",Name="Ljubljana Polje"},
                    new City {Code="1261",Name="Ljubljana-Dobrunje"},
                    new City {Code="1262",Name="Dol pri Ljubljani"},
                    new City {Code="1270",Name="Litija"},
                    new City {Code="1272",Name="Polšnik"},
                    new City {Code="1273",Name="Dole pri Litiji"},
                    new City {Code="1274",Name="Gabrovka"},
                    new City {Code="1275",Name="Šmartno pri Litiji"},
                    new City {Code="1276",Name="Primskovo"},
                    new City {Code="1281",Name="Kresnice"},
                    new City {Code="1282",Name="Sava"},
                    new City {Code="1290",Name="Grosuplje"},
                    new City {Code="1291",Name="Škofljica"},
                    new City {Code="1292",Name="Ig"},
                    new City {Code="1293",Name="Šmarje-Sap"},
                    new City {Code="1294",Name="Višnja Gora"},
                    new City {Code="1295",Name="Ivančna Gorica"},
                    new City {Code="1296",Name="Šentvid pri Stični"},
                    new City {Code="1301",Name="Krka"},
                    new City {Code="1303",Name="Zagradec"},
                    new City {Code="1310",Name="Ribnica"},
                    new City {Code="1311",Name="Turjak"},
                    new City {Code="1312",Name="Videm-Dobrepolje"},
                    new City {Code="1313",Name="Struge"},
                    new City {Code="1314",Name="Rob"},
                    new City {Code="1315",Name="Velike Lašče"},
                    new City {Code="1316",Name="Ortnek"},
                    new City {Code="1317",Name="Sodražica"},
                    new City {Code="1318",Name="Loški Potok"},
                    new City {Code="1319",Name="Draga"},
                    new City {Code="1330",Name="Kočevje"},
                    new City {Code="1331",Name="Dolenja vas"},
                    new City {Code="1332",Name="Stara Cerkev"},
                    new City {Code="1336",Name="Vas"},
                    new City {Code="1337",Name="Osilnica"},
                    new City {Code="1338",Name="Kočevska Reka"},
                    new City {Code="1351",Name="Brezovica pri Ljubljani"},
                    new City {Code="1352",Name="Preserje"},
                    new City {Code="1353",Name="Borovnica"},
                    new City {Code="1354",Name="Horjul"},
                    new City {Code="1355",Name="Polhov Gradec"},
                    new City {Code="1356",Name="Dobrova"},
                    new City {Code="1357",Name="Notranje Gorice"},
                    new City {Code="1358",Name="Log pri Brezovici"},
                    new City {Code="1360",Name="Vrhnika"},
                    new City {Code="1370",Name="Logatec"},
                    new City {Code="1371",Name="Logatec"},
                    new City {Code="1372",Name="Hotedršica"},
                    new City {Code="1373",Name="Rovte"},
                    new City {Code="1380",Name="Cerknica"},
                    new City {Code="1381",Name="Rakek"},
                    new City {Code="1382",Name="Begunje pri Cerknici"},
                    new City {Code="1384",Name="Grahovo"},
                    new City {Code="1385",Name="Nova vas"},
                    new City {Code="1386",Name="Stari trg pri Ložu"},
                    new City {Code="1410",Name="Zagorje ob Savi"},
                    new City {Code="1411",Name="Izlake"},
                    new City {Code="1412",Name="Kisovec"},
                    new City {Code="1413",Name="Čemšenik"},
                    new City {Code="1414",Name="Podkum"},
                    new City {Code="1420",Name="Trbovlje"},
                    new City {Code="1423",Name="Dobovec"},
                    new City {Code="1430",Name="Hrastnik"},
                    new City {Code="1431",Name="Dol pri Hrastniku"},
                    new City {Code="1432",Name="Zidani Most"},
                    new City {Code="1433",Name="Radeče"},
                    new City {Code="1434",Name="Loka pri Zidanem Mostu"},
                    new City {Code="1500",Name="Ljubljana"},
                    new City {Code="1501",Name="Ljubljana"},
                    new City {Code="1502",Name="Ljubljana"},
                    new City {Code="1503",Name="Ljubljana"},
                    new City {Code="1504",Name="Ljubljana"},
                    new City {Code="1505",Name="Ljubljana"},
                    new City {Code="1506",Name="Ljubljana"},
                    new City {Code="1507",Name="Ljubljana"},
                    new City {Code="1508",Name="Ljubljana"},
                    new City {Code="1509",Name="Ljubljana"},
                    new City {Code="1510",Name="Ljubljana"},
                    new City {Code="1511",Name="Ljubljana"},
                    new City {Code="1512",Name="Ljubljana"},
                    new City {Code="1513",Name="Ljubljana"},
                    new City {Code="1514",Name="Ljubljana"},
                    new City {Code="1516",Name="Ljubljana"},
                    new City {Code="1517",Name="Ljubljana"},
                    new City {Code="1518",Name="Ljubljana"},
                    new City {Code="1519",Name="Ljubljana"},
                    new City {Code="1520",Name="Ljubljana"},
                    new City {Code="1521",Name="Ljubljana"},
                    new City {Code="1522",Name="Ljubljana"},
                    new City {Code="1523",Name="Ljubljana"},
                    new City {Code="1524",Name="Ljubljana"},
                    new City {Code="1525",Name="Ljubljana"},
                    new City {Code="1526",Name="Ljubljana"},
                    new City {Code="1527",Name="Ljubljana"},
                    new City {Code="1528",Name="Ljubljana"},
                    new City {Code="1529",Name="Ljubljana"},
                    new City {Code="1532",Name="Ljubljana"},
                    new City {Code="1533",Name="Ljubljana"},
                    new City {Code="1534",Name="Ljubljana"},
                    new City {Code="1535",Name="Ljubljana"},
                    new City {Code="1536",Name="Ljubljana"},
                    new City {Code="1537",Name="Ljubljana"},
                    new City {Code="1538",Name="Ljubljana"},
                    new City {Code="1540",Name="Ljubljana"},
                    new City {Code="1542",Name="Ljubljana"},
                    new City {Code="1543",Name="Ljubljana"},
                    new City {Code="1544",Name="Ljubljana"},
                    new City {Code="1545",Name="Ljubljana"},
                    new City {Code="1546",Name="Ljubljana"},
                    new City {Code="1547",Name="Ljubljana"},
                    new City {Code="1550",Name="Ljubljana"},
                    new City {Code="1600",Name="Ljubljana"},
                    new City {Code="2000",Name="Maribor - dostava"},
                    new City {Code="2001",Name="Maribor - poštni predali"},
                    new City {Code="2201",Name="Zgornja Kungota"},
                    new City {Code="2204",Name="Miklavž na Dravskem polju"},
                    new City {Code="2205",Name="Starše"},
                    new City {Code="2206",Name="Marjeta na Dravskem polju"},
                    new City {Code="2208",Name="Pohorje"},
                    new City {Code="2211",Name="Pesnica pri Mariboru"},
                    new City {Code="2212",Name="Šentilj v Slovenskih goricah"},
                    new City {Code="2213",Name="Zgornja Velka"},
                    new City {Code="2214",Name="Sladki Vrh"},
                    new City {Code="2215",Name="Ceršak"},
                    new City {Code="2221",Name="Jarenina"},
                    new City {Code="2222",Name="Jakobski Dol"},
                    new City {Code="2223",Name="Jurovski Dol"},
                    new City {Code="2229",Name="Malečnik"},
                    new City {Code="2230",Name="Lenart v Slovenskih goricah"},
                    new City {Code="2231",Name="Pernica"},
                    new City {Code="2232",Name="Voličina"},
                    new City {Code="2233",Name="Sv. Ana v Slovenskih goricah"},
                    new City {Code="2234",Name="Benedikt"},
                    new City {Code="2235",Name="Sv. Trojica v Slovenskih goricah"},
                    new City {Code="2236",Name="Cerkvenjak"},
                    new City {Code="2241",Name="Spodnji Duplek"},
                    new City {Code="2242",Name="Zgornja Korena"},
                    new City {Code="2250",Name="Ptuj"},
                    new City {Code="2252",Name="Dornava"},
                    new City {Code="2253",Name="Destrnik"},
                    new City {Code="2254",Name="Trnovska vas"},
                    new City {Code="2255",Name="Vitomarci"},
                    new City {Code="2256",Name="Juršinci"},
                    new City {Code="2257",Name="Polenšak"},
                    new City {Code="2258",Name="Sveti Tomaž"},
                    new City {Code="2259",Name="Ivanjkovci"},
                    new City {Code="2270",Name="Ormož"},
                    new City {Code="2272",Name="Gorišnica"},
                    new City {Code="2273",Name="Podgorci"},
                    new City {Code="2274",Name="Velika Nedelja"},
                    new City {Code="2275",Name="Miklavž pri Ormožu"},
                    new City {Code="2276",Name="Kog"},
                    new City {Code="2277",Name="Središče ob Dravi"},
                    new City {Code="2281",Name="Markovci"},
                    new City {Code="2282",Name="Cirkulane"},
                    new City {Code="2283",Name="Zavrč"},
                    new City {Code="2284",Name="Videm pri Ptuju"},
                    new City {Code="2285",Name="Zgornji Leskovec"},
                    new City {Code="2286",Name="Podlehnik"},
                    new City {Code="2287",Name="Žetale"},
                    new City {Code="2288",Name="Hajdina"},
                    new City {Code="2289",Name="Stoperce"},
                    new City {Code="2310",Name="Slovenska Bistrica"},
                    new City {Code="2311",Name="Hoče"},
                    new City {Code="2312",Name="Orehova vas"},
                    new City {Code="2313",Name="Fram"},
                    new City {Code="2314",Name="Zgornja Polskava"},
                    new City {Code="2315",Name="Šmartno na Pohorju"},
                    new City {Code="2316",Name="Zgornja Ložnica"},
                    new City {Code="2317",Name="Oplotnica"},
                    new City {Code="2318",Name="Laporje"},
                    new City {Code="2319",Name="Poljčane"},
                    new City {Code="2321",Name="Makole"},
                    new City {Code="2322",Name="Majšperk"},
                    new City {Code="2323",Name="Ptujska Gora"},
                    new City {Code="2324",Name="Lovrenc na Dravskem polju"},
                    new City {Code="2325",Name="Kidričevo"},
                    new City {Code="2326",Name="Cirkovce"},
                    new City {Code="2327",Name="Rače"},
                    new City {Code="2331",Name="Pragersko"},
                    new City {Code="2341",Name="Limbuš"},
                    new City {Code="2342",Name="Ruše"},
                    new City {Code="2343",Name="Fala"},
                    new City {Code="2344",Name="Lovrenc na Pohorju"},
                    new City {Code="2345",Name="Bistrica ob Dravi"},
                    new City {Code="2351",Name="Kamnica"},
                    new City {Code="2352",Name="Selnica ob Dravi"},
                    new City {Code="2353",Name="Sv. Duh na Ostrem Vrhu"},
                    new City {Code="2354",Name="Bresternica"},
                    new City {Code="2360",Name="Radlje ob Dravi"},
                    new City {Code="2361",Name="Ožbalt"},
                    new City {Code="2362",Name="Kapla"},
                    new City {Code="2363",Name="Podvelka"},
                    new City {Code="2364",Name="Ribnica na Pohorju"},
                    new City {Code="2365",Name="Vuhred"},
                    new City {Code="2366",Name="Muta"},
                    new City {Code="2367",Name="Vuzenica"},
                    new City {Code="2370",Name="Dravograd"},
                    new City {Code="2371",Name="Trbonje"},
                    new City {Code="2372",Name="Libeliče"},
                    new City {Code="2373",Name="Šentjanž pri Dravogradu"},
                    new City {Code="2380",Name="Slovenj Gradec"},
                    new City {Code="2381",Name="Podgorje pri Slovenj Gradcu"},
                    new City {Code="2382",Name="Mislinja"},
                    new City {Code="2383",Name="Šmartno pri Slovenj Gradcu"},
                    new City {Code="2390",Name="Ravne na Koroškem"},
                    new City {Code="2391",Name="Prevalje"},
                    new City {Code="2392",Name="Mežica"},
                    new City {Code="2393",Name="Črna na Koroškem"},
                    new City {Code="2394",Name="Kotlje"},
                    new City {Code="2500",Name="Maribor"},
                    new City {Code="2501",Name="Maribor"},
                    new City {Code="2502",Name="Maribor"},
                    new City {Code="2503",Name="Maribor"},
                    new City {Code="2504",Name="Maribor"},
                    new City {Code="2505",Name="Maribor"},
                    new City {Code="2506",Name="Maribor"},
                    new City {Code="2507",Name="Maribor"},
                    new City {Code="2509",Name="Maribor"},
                    new City {Code="2600",Name="Maribor"},
                    new City {Code="2603",Name="Maribor"},
                    new City {Code="2609",Name="Maribor"},
                    new City {Code="2610",Name="Maribor"},
                    new City {Code="3000",Name="Celje - dostava"},
                    new City {Code="3001",Name="Celje - poštni predali"},
                    new City {Code="3201",Name="Šmartno v Rožni dolini"},
                    new City {Code="3202",Name="Ljubečna"},
                    new City {Code="3203",Name="Nova Cerkev"},
                    new City {Code="3204",Name="Dobrna"},
                    new City {Code="3205",Name="Vitanje"},
                    new City {Code="3206",Name="Stranice"},
                    new City {Code="3210",Name="Slovenske Konjice"},
                    new City {Code="3211",Name="Škofja vas"},
                    new City {Code="3212",Name="Vojnik"},
                    new City {Code="3213",Name="Frankolovo"},
                    new City {Code="3214",Name="Zreče"},
                    new City {Code="3215",Name="Loče"},
                    new City {Code="3220",Name="Štore"},
                    new City {Code="3221",Name="Teharje"},
                    new City {Code="3222",Name="Dramlje"},
                    new City {Code="3223",Name="Loka pri Žusmu"},
                    new City {Code="3224",Name="Dobje pri Planini"},
                    new City {Code="3225",Name="Planina pri Sevnici"},
                    new City {Code="3230",Name="Šentjur"},
                    new City {Code="3231",Name="Grobelno"},
                    new City {Code="3232",Name="Ponikva"},
                    new City {Code="3233",Name="Kalobje"},
                    new City {Code="3240",Name="Šmarje pri Jelšah"},
                    new City {Code="3241",Name="Podplat"},
                    new City {Code="3250",Name="Rogaška Slatina"},
                    new City {Code="3252",Name="Rogatec"},
                    new City {Code="3253",Name="Pristava pri Mestinju"},
                    new City {Code="3254",Name="Podčetrtek"},
                    new City {Code="3255",Name="Buče"},
                    new City {Code="3256",Name="Bistrica ob Sotli"},
                    new City {Code="3257",Name="Podsreda"},
                    new City {Code="3260",Name="Kozje"},
                    new City {Code="3261",Name="Lesično"},
                    new City {Code="3262",Name="Prevorje"},
                    new City {Code="3263",Name="Gorica pri Slivnici"},
                    new City {Code="3264",Name="Sveti Štefan"},
                    new City {Code="3270",Name="Laško"},
                    new City {Code="3271",Name="Šentrupert"},
                    new City {Code="3272",Name="Rimske Toplice"},
                    new City {Code="3273",Name="Jurklošter"},
                    new City {Code="3301",Name="Petrovče"},
                    new City {Code="3302",Name="Griže"},
                    new City {Code="3303",Name="Gomilsko"},
                    new City {Code="3304",Name="Tabor"},
                    new City {Code="3305",Name="Vransko"},
                    new City {Code="3310",Name="Žalec"},
                    new City {Code="3311",Name="Šempeter v Savinjski dolini"},
                    new City {Code="3312",Name="Prebold"},
                    new City {Code="3313",Name="Polzela"},
                    new City {Code="3314",Name="Braslovče"},
                    new City {Code="3320",Name="Velenje - dostava"},
                    new City {Code="3322",Name="Velenje - poštni predali"},
                    new City {Code="3325",Name="Šoštanj"},
                    new City {Code="3326",Name="Topolšica"},
                    new City {Code="3327",Name="Šmartno ob Paki"},
                    new City {Code="3330",Name="Mozirje"},
                    new City {Code="3331",Name="Nazarje"},
                    new City {Code="3332",Name="Rečica ob Savinji"},
                    new City {Code="3333",Name="Ljubno ob Savinji"},
                    new City {Code="3334",Name="Luče"},
                    new City {Code="3335",Name="Solčava"},
                    new City {Code="3341",Name="Šmartno ob Dreti"},
                    new City {Code="3342",Name="Gornji Grad"},
                    new City {Code="3502",Name="Celje"},
                    new City {Code="3503",Name="Velenje"},
                    new City {Code="3504",Name="Velenje"},
                    new City {Code="3505",Name="Celje"},
                    new City {Code="3600",Name="Celje"},
                    new City {Code="4000",Name="Kranj - dostava"},
                    new City {Code="4001",Name="Kranj - poštni predali"},
                    new City {Code="4201",Name="Zgornja Besnica"},
                    new City {Code="4202",Name="Naklo"},
                    new City {Code="4203",Name="Duplje"},
                    new City {Code="4204",Name="Golnik"},
                    new City {Code="4205",Name="Preddvor"},
                    new City {Code="4206",Name="Zgornje Jezersko"},
                    new City {Code="4207",Name="Cerklje na Gorenjskem"},
                    new City {Code="4208",Name="Šenčur"},
                    new City {Code="4209",Name="Žabnica"},
                    new City {Code="4210",Name="Brnik aerodrom"},
                    new City {Code="4211",Name="Mavčiče"},
                    new City {Code="4212",Name="Visoko"},
                    new City {Code="4220",Name="Škofja Loka"},
                    new City {Code="4223",Name="Poljane nad Škofjo Loko"},
                    new City {Code="4224",Name="Gorenja vas"},
                    new City {Code="4225",Name="Sovodenj"},
                    new City {Code="4226",Name="Žiri"},
                    new City {Code="4227",Name="Selca"},
                    new City {Code="4228",Name="Železniki"},
                    new City {Code="4229",Name="Sorica"},
                    new City {Code="4240",Name="Radovljica"},
                    new City {Code="4243",Name="Brezje"},
                    new City {Code="4244",Name="Podnart"},
                    new City {Code="4245",Name="Kropa"},
                    new City {Code="4246",Name="Kamna Gorica"},
                    new City {Code="4247",Name="Zgornje Gorje"},
                    new City {Code="4248",Name="Lesce"},
                    new City {Code="4260",Name="Bled"},
                    new City {Code="4263",Name="Bohinjska Bela"},
                    new City {Code="4264",Name="Bohinjska Bistrica"},
                    new City {Code="4265",Name="Bohinjsko jezero"},
                    new City {Code="4267",Name="Srednja vas v Bohinju"},
                    new City {Code="4270",Name="Jesenice"},
                    new City {Code="4273",Name="Blejska Dobrava"},
                    new City {Code="4274",Name="Žirovnica"},
                    new City {Code="4275",Name="Begunje na Gorenjskem"},
                    new City {Code="4276",Name="Hrušica"},
                    new City {Code="4280",Name="Kranjska Gora"},
                    new City {Code="4281",Name="Mojstrana"},
                    new City {Code="4282",Name="Gozd Martuljek"},
                    new City {Code="4283",Name="Rateče-Planica"},
                    new City {Code="4290",Name="Tržič"},
                    new City {Code="4294",Name="Križe"},
                    new City {Code="4501",Name="Naklo"},
                    new City {Code="4502",Name="Kranj"},
                    new City {Code="4600",Name="Kranj"},
                    new City {Code="5000",Name="Nova Gorica - dostava"},
                    new City {Code="5001",Name="Nova Gorica - poštni predali"},
                    new City {Code="5210",Name="Deskle"},
                    new City {Code="5211",Name="Kojsko"},
                    new City {Code="5212",Name="Dobrovo v Brdih"},
                    new City {Code="5213",Name="Kanal"},
                    new City {Code="5214",Name="Kal nad Kanalom"},
                    new City {Code="5215",Name="Ročinj"},
                    new City {Code="5216",Name="Most na Soči"},
                    new City {Code="5220",Name="Tolmin"},
                    new City {Code="5222",Name="Kobarid"},
                    new City {Code="5223",Name="Breginj"},
                    new City {Code="5224",Name="Srpenica"},
                    new City {Code="5230",Name="Bovec"},
                    new City {Code="5231",Name="Log pod Mangartom"},
                    new City {Code="5232",Name="Soča"},
                    new City {Code="5242",Name="Grahovo ob Bači"},
                    new City {Code="5243",Name="Podbrdo"},
                    new City {Code="5250",Name="Solkan"},
                    new City {Code="5251",Name="Grgar"},
                    new City {Code="5252",Name="Trnovo pri Gorici"},
                    new City {Code="5253",Name="Čepovan"},
                    new City {Code="5261",Name="Šempas"},
                    new City {Code="5262",Name="Črniče"},
                    new City {Code="5263",Name="Dobravlje"},
                    new City {Code="5270",Name="Ajdovščina"},
                    new City {Code="5271",Name="Vipava"},
                    new City {Code="5272",Name="Podnanos"},
                    new City {Code="5273",Name="Col"},
                    new City {Code="5274",Name="Črni Vrh nad Idrijo"},
                    new City {Code="5275",Name="Godovič"},
                    new City {Code="5280",Name="Idrija"},
                    new City {Code="5281",Name="Spodnja Idrija"},
                    new City {Code="5282",Name="Cerkno"},
                    new City {Code="5283",Name="Slap ob Idrijci"},
                    new City {Code="5290",Name="Šempeter pri Gorici"},
                    new City {Code="5291",Name="Miren"},
                    new City {Code="5292",Name="Renče"},
                    new City {Code="5293",Name="Volčja Draga"},
                    new City {Code="5294",Name="Dornberk"},
                    new City {Code="5295",Name="Branik"},
                    new City {Code="5296",Name="Kostanjevica na Krasu"},
                    new City {Code="5297",Name="Prvačina"},
                    new City {Code="5600",Name="Nova Gorica"},
                    new City {Code="6000",Name="Koper/Capodistria - dostava"},
                    new City {Code="6001",Name="Koper/Capodistria - poštni predali"},
                    new City {Code="6210",Name="Sežana"},
                    new City {Code="6215",Name="Divača"},
                    new City {Code="6216",Name="Podgorje"},
                    new City {Code="6217",Name="Vremski Britof"},
                    new City {Code="6219",Name="Lokev"},
                    new City {Code="6221",Name="Dutovlje"},
                    new City {Code="6222",Name="Štanjel"},
                    new City {Code="6223",Name="Komen"},
                    new City {Code="6224",Name="Senožeče"},
                    new City {Code="6225",Name="Hruševje"},
                    new City {Code="6230",Name="Postojna"},
                    new City {Code="6232",Name="Planina"},
                    new City {Code="6240",Name="Kozina"},
                    new City {Code="6242",Name="Materija"},
                    new City {Code="6243",Name="Obrov"},
                    new City {Code="6244",Name="Podgrad"},
                    new City {Code="6250",Name="Ilirska Bistrica"},
                    new City {Code="6251",Name="Ilirska Bistrica-Trnovo"},
                    new City {Code="6253",Name="Knežak"},
                    new City {Code="6254",Name="Jelšane"},
                    new City {Code="6255",Name="Prem"},
                    new City {Code="6256",Name="Košana"},
                    new City {Code="6257",Name="Pivka"},
                    new City {Code="6258",Name="Prestranek"},
                    new City {Code="6271",Name="Dekani"},
                    new City {Code="6272",Name="Gračišče"},
                    new City {Code="6273",Name="Marezige"},
                    new City {Code="6274",Name="Šmarje"},
                    new City {Code="6275",Name="Črni Kal"},
                    new City {Code="6276",Name="Pobegi"},
                    new City {Code="6280",Name="Ankaran/Ancarano"},
                    new City {Code="6281",Name="Škofije"},
                    new City {Code="6310",Name="Izola/Isola"},
                    new City {Code="6320",Name="Portorož/Portorose"},
                    new City {Code="6323",Name="Strunjan/Strugnano (sezonska pošta)"},
                    new City {Code="6330",Name="Piran/Pirano"},
                    new City {Code="6333",Name="Sečovlje/Sicciole"},
                    new City {Code="6501",Name="Koper"},
                    new City {Code="6502",Name="Koper"},
                    new City {Code="6503",Name="Koper"},
                    new City {Code="6504",Name="Koper"},
                    new City {Code="6505",Name="Koper"},
                    new City {Code="6600",Name="Koper"},
                    new City {Code="8000",Name="Novo mesto - dostava"},
                    new City {Code="8001",Name="Novo mesto - poštni predali"},
                    new City {Code="8210",Name="Trebnje"},
                    new City {Code="8211",Name="Dobrnič"},
                    new City {Code="8212",Name="Velika Loka"},
                    new City {Code="8213",Name="Veliki Gaber"},
                    new City {Code="8216",Name="Mirna Peč"},
                    new City {Code="8220",Name="Šmarješke Toplice"},
                    new City {Code="8222",Name="Otočec"},
                    new City {Code="8230",Name="Mokronog"},
                    new City {Code="8231",Name="Trebelno"},
                    new City {Code="8232",Name="Šentrupert"},
                    new City {Code="8233",Name="Mirna"},
                    new City {Code="8250",Name="Brežice"},
                    new City {Code="8251",Name="Čatež ob Savi"},
                    new City {Code="8253",Name="Artiče"},
                    new City {Code="8254",Name="Globoko"},
                    new City {Code="8255",Name="Pišece"},
                    new City {Code="8256",Name="Sromlje"},
                    new City {Code="8257",Name="Dobova"},
                    new City {Code="8258",Name="Kapele"},
                    new City {Code="8259",Name="Bizeljsko"},
                    new City {Code="8261",Name="Jesenice na Dolenjskem"},
                    new City {Code="8262",Name="Krška vas"},
                    new City {Code="8263",Name="Cerklje ob Krki"},
                    new City {Code="8270",Name="Krško"},
                    new City {Code="8272",Name="Zdole"},
                    new City {Code="8273",Name="Leskovec pri Krškem"},
                    new City {Code="8274",Name="Raka"},
                    new City {Code="8275",Name="Škocjan"},
                    new City {Code="8276",Name="Bučka"},
                    new City {Code="8280",Name="Brestanica"},
                    new City {Code="8281",Name="Senovo"},
                    new City {Code="8282",Name="Koprivnica"},
                    new City {Code="8283",Name="Blanca"},
                    new City {Code="8290",Name="Sevnica"},
                    new City {Code="8292",Name="Zabukovje"},
                    new City {Code="8293",Name="Studenec"},
                    new City {Code="8294",Name="Boštanj"},
                    new City {Code="8295",Name="Tržišče"},
                    new City {Code="8296",Name="Krmelj"},
                    new City {Code="8297",Name="Šentjanž"},
                    new City {Code="8310",Name="Šentjernej"},
                    new City {Code="8311",Name="Kostanjevica na Krki"},
                    new City {Code="8312",Name="Podbočje"},
                    new City {Code="8321",Name="Brusnice"},
                    new City {Code="8322",Name="Stopiče"},
                    new City {Code="8323",Name="Uršna sela"},
                    new City {Code="8330",Name="Metlika"},
                    new City {Code="8331",Name="Suhor"},
                    new City {Code="8332",Name="Gradac"},
                    new City {Code="8333",Name="Semič"},
                    new City {Code="8340",Name="Črnomelj"},
                    new City {Code="8341",Name="Adlešiči"},
                    new City {Code="8342",Name="Stari trg ob Kolpi"},
                    new City {Code="8343",Name="Dragatuš"},
                    new City {Code="8344",Name="Vinica"},
                    new City {Code="8350",Name="Dolenjske Toplice"},
                    new City {Code="8351",Name="Straža"},
                    new City {Code="8360",Name="Žužemberk"},
                    new City {Code="8361",Name="Dvor"},
                    new City {Code="8362",Name="Hinje"},
                    new City {Code="8501",Name="Novo mesto"},
                    new City {Code="8600",Name="Novo mesto"},
                    new City {Code="9000",Name="Murska Sobota - dostava"},
                    new City {Code="9001",Name="Murska Sobota - poštni predali"},
                    new City {Code="9201",Name="Puconci"},
                    new City {Code="9202",Name="Mačkovci"},
                    new City {Code="9203",Name="Petrovci"},
                    new City {Code="9204",Name="Šalovci"},
                    new City {Code="9205",Name="Hodoš/Hodos"},
                    new City {Code="9206",Name="Križevci"},
                    new City {Code="9207",Name="Prosenjakovci/Partosfalva"},
                    new City {Code="9208",Name="Fokovci"},
                    new City {Code="9220",Name="Lendava/Lendva"},
                    new City {Code="9221",Name="Martjanci"},
                    new City {Code="9222",Name="Bogojina"},
                    new City {Code="9223",Name="Dobrovnik/Dobronak"},
                    new City {Code="9224",Name="Turnišče"},
                    new City {Code="9225",Name="Velika Polana"},
                    new City {Code="9226",Name="Moravske Toplice"},
                    new City {Code="9227",Name="Kobilje"},
                    new City {Code="9231",Name="Beltinci"},
                    new City {Code="9232",Name="Črenšovci"},
                    new City {Code="9233",Name="Odranci"},
                    new City {Code="9240",Name="Ljutomer"},
                    new City {Code="9241",Name="Veržej"},
                    new City {Code="9242",Name="Križevci pri Ljutomeru"},
                    new City {Code="9243",Name="Mala Nedelja"},
                    new City {Code="9244",Name="Sv. Jurij ob Ščavnici"},
                    new City {Code="9245",Name="Spodnji Ivanjci"},
                    new City {Code="9250",Name="Gornja Radgona"},
                    new City {Code="9251",Name="Tišina"},
                    new City {Code="9252",Name="Radenci"},
                    new City {Code="9253",Name="Apače"},
                    new City {Code="9261",Name="Cankova"},
                    new City {Code="9262",Name="Rogašovci"},
                    new City {Code="9263",Name="Kuzma"},
                    new City {Code="9264",Name="Grad"},
                    new City {Code="9265",Name="Bodonci"},
                    new City {Code="9501",Name="Murska Sobota"},
                    new City {Code="9502",Name="Radenci"},
                    new City {Code="9600",Name="Murska Sobota"},
                };

                context.Cities.AddRange(cities);
                context.SaveChanges();
            }

            // Add streets.
            if (!context.Streets.Any())
            {
                var streets = new Street[]
                {
                    new Street{CityCode="1000", StreetName="Celovška ulica", StreetNumber=13},
                    new Street{CityCode="2000", StreetName="Maistrova", StreetNumber=26},
                    new Street{CityCode="6000", StreetName="Prekomorska ulica", StreetNumber=4},
                    new Street{CityCode="8000", StreetName="Ciganska ulica", StreetNumber=46},
                };

                context.Streets.AddRange(streets);
                context.SaveChanges();
            }

            // Add branches.
            if (!context.Branches.Any())
            {
                var branches = new Branch[]
                {
                    new Branch{Name="Skladišče LJ", CityCode="1000", StreetName="Celovška ulica", StreetNumber=13},
                    new Branch{Name="Skladišče MB", CityCode="2000", StreetName="Maistrova", StreetNumber=26},
                    new Branch{Name="Skladišče KP", CityCode="6000", StreetName="Prekomorska ulica", StreetNumber=4},
                    new Branch{Name="Skladišče NM", CityCode="8000", StreetName="Ciganska ulica", StreetNumber=46},
                };

                context.Branches.AddRange(branches);
                context.SaveChanges();
            }

            // Add parcel statuses.
            if (!context.ParcelStatuses.Any())
            {
                var statuses = new ParcelStatus[]
                {
                    //new ParcelStatus{ID=1,Name="In IT system"},
                    new ParcelStatus{ID=1,Name="In transit"},
                    new ParcelStatus{ID=2,Name="At the final parcel center"},
                    new ParcelStatus{ID=3,Name="In delivery"},
                    new ParcelStatus{ID=4,Name="Delivered"},
                };

                context.ParcelStatuses.AddRange(statuses);
                context.SaveChanges();
            }

            // Add job statuses.
            if (!context.JobStatuses.Any())
            {
                var statuses = new JobStatus[]
                {
                    new JobStatus{ID=1, Name="Pending"},
                    new JobStatus{ID=2, Name="Completed"},
                    new JobStatus{ID=3, Name="Cancelled"},
                };

                context.JobStatuses.AddRange(statuses);
                context.SaveChanges();
            }

            // Add job types.
            if (!context.JobTypes.Any())
            {
                var types = new JobType[]
                {
                    new JobType{ID=1, Name="Order processing"},
                    new JobType{ID=2, Name="Handover"},
                    new JobType{ID=3, Name="Check in"},
                    new JobType{ID=4, Name="Check out"},
                    new JobType{ID=5, Name="Cargo departing confirmation"},
                    new JobType{ID=6, Name="Cargo arrival confirmation"},
                    new JobType{ID=7, Name="Delivery cargo confirmation"},
                    new JobType{ID=8, Name="Parcel handover"},
                };

                context.JobTypes.AddRange(types);
                context.SaveChanges();
            }

            // Add roles.
            var roles = new IdentityRole[] {
                new IdentityRole{Id="1", Name="Administrator"},
                new IdentityRole{Id="2", Name="Warehouse manager"},
                new IdentityRole{Id="3", Name="Warehouse worker"},
                new IdentityRole{Id="4", Name="Logistics agent"},
                new IdentityRole{Id="5", Name="Delivery driver"},
            };

            foreach (IdentityRole r in roles)
            {
                if (!context.Roles.Any(rl => rl.Id == r.Id))
                {
                    context.Roles.Add(r);
                }
            }

            // Add application users.
            var users = new ApplicationUser[]
            {
                new ApplicationUser
                {
                    Name = "Klemen",
                    Surname = "Podgoršek",
                    Email = "klemen.podgorsek@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "klemen.podgorsek@sfl.si",
                    NormalizedUserName = "klemen.podgorsek@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Anže",
                    Surname = "Grčar",
                    Email = "anze.grcar@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "anze.grcar@sfl.si",
                    NormalizedUserName = "anze.grcar@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Jan",
                    Surname = "Kotnik",
                    Email = "jan.kotnik@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "jan.kotnik@sfl.si",
                    NormalizedUserName = "jan.kotnik@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Darjan",
                    Surname = "Toth",
                    Email = "darjan.toth@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "darjan.toth@sfl.si",
                    NormalizedUserName = "darjan.toth@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },

                new ApplicationUser
                {
                    Name = "Rudi",
                    Surname = "Strela",
                    Email = "rudi.strela@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "rudi.strela@sfl.si",
                    NormalizedUserName = "rudi.strela@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Kristjan",
                    Surname = "Kolenc",
                    Email = "kristjan.kolenc@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "kristjan.kolenc@sfl.si",
                    NormalizedUserName = "kristjan.kolenc@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Ludvik",
                    Surname = "Plemeniti",
                    Email = "ludvik.plemeniti@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "ludvik.plemeniti@sfl.si",
                    NormalizedUserName = "ludvik.plemeniti@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },

                new ApplicationUser
                {
                    Name = "Marjan",
                    Surname = "Udarni",
                    Email = "marjan.udarni@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "marjan.udarni@sfl.si",
                    NormalizedUserName = "marjan.udarni@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Mitja",
                    Surname = "Veliki",
                    Email = "mitja.veliki@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "mitja.veliki@sfl.si",
                    NormalizedUserName = "mitja.veliki@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Toni",
                    Surname = "Fer",
                    Email = "toni.fer@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "toni.fer@sfl.si",
                    NormalizedUserName = "toni.fer@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },

                new ApplicationUser
                {
                    Name = "Branko",
                    Surname = "Bojc",
                    Email = "branko.bojc@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "branko.bojc@sfl.si",
                    NormalizedUserName = "branko.bojc@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Alojz",
                    Surname = "Mihelič",
                    Email = "alojz.mihelič@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "alojz.mihelič@sfl.si",
                    NormalizedUserName = "alojz.mihelič@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
                new ApplicationUser
                {
                    Name = "Marija",
                    Surname = "Vzvišena",
                    Email = "marija.vzvišena@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "marija.vzvišena@sfl.si",
                    NormalizedUserName = "marija.vzvišena@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },

                new ApplicationUser
                {
                    Name = "Otzi",
                    Surname = "Tribalni",
                    Email = "otzi.tribalni@sfl.si",
                    NormalizedEmail = "XXXX@SLF.SI",
                    UserName = "otzi.tribalni@sfl.si",
                    NormalizedUserName = "otzi.tribalni@sfl.si",
                    PhoneNumber = "+111111111111",
                    EmailConfirmed = true,
                    PhoneNumberConfirmed = true,
                    SecurityStamp = Guid.NewGuid().ToString("D")
                },
            };

            for (int i = 0; i < users.Length; i++)
            {
                var user = users[i];
                if (!context.Users.Any(u => u.UserName == user.UserName))
                {
                    var password = new PasswordHasher<ApplicationUser>();
                    var hashed = password.HashPassword(user, "Testni123!");
                    user.PasswordHash = hashed;
                    context.Users.Add(user);
                }
            }

            context.SaveChanges();

            // Add staff.
            var staff = new Staff[] {
                new Staff {Username=users[0].UserName, Name="Klemen", Surname="Podgoršek", BranchID=1, RoleID=5},
                new Staff {Username=users[1].UserName, Name="Anže", Surname="Grčar", BranchID=1, RoleID=3},
                new Staff {Username=users[2].UserName, Name="Jan", Surname="Kotnik", BranchID=1, RoleID=2},
                new Staff {Username=users[3].UserName, Name="Darjan", Surname="Toth", BranchID=1, RoleID=4},

                new Staff {Username=users[4].UserName, Name="Rudi", Surname="Strela", BranchID=2, RoleID=5},
                new Staff {Username=users[5].UserName, Name="Kristjan", Surname="Kolenc", BranchID=2, RoleID=3},
                new Staff {Username=users[6].UserName, Name="Ludvik", Surname="Plemeniti", BranchID=2, RoleID=2},


                new Staff {Username=users[7].UserName, Name="Marjan", Surname="Udarni", BranchID=3, RoleID=5},
                new Staff {Username=users[8].UserName, Name="Mitja", Surname="Veliki", BranchID=3, RoleID=3},
                new Staff {Username=users[9].UserName, Name="Toni", Surname="Fer", BranchID=3, RoleID=2},

                new Staff {Username=users[10].UserName, Name="Banko", Surname="Bojc", BranchID=4, RoleID=5},
                new Staff {Username=users[11].UserName, Name="Alojz", Surname="Mihelič", BranchID=4, RoleID=3},
                new Staff {Username=users[12].UserName, Name="Marija", Surname="Vzvišena", BranchID=4, RoleID=2},

                new Staff {Username=users[13].UserName, Name="Otzi", Surname="Tribalni", BranchID=1, RoleID=1},
            };

            foreach (var stf in staff)
            {
                if (!context.Staff.Any(s => s.Username == stf.Username))
                {
                    context.Staff.Add(stf);
                }
            }

            context.SaveChanges();

            // Add user roles.
            for (int i = 0; i < users.Length; i++)
            {
                var user = users[i];
                var roleID = context.Staff
                    .Select(s => s)
                    .Where(s => s.Username == user.UserName)
                    .First()
                    .RoleID;
                var userID = context.Users
                    .Select(u => u)
                    .Where(u => u.UserName == user.UserName)
                    .First()
                    .Id;

                var UserRoles = new IdentityUserRole<string>[]
                {
                    new IdentityUserRole<string>{RoleId=roleID.ToString(), UserId=userID},
                };

                foreach (IdentityUserRole<string> r in UserRoles)
                {
                    if (!context.UserRoles.Any(ur => ur.UserId == user.Id))
                    {
                        context.UserRoles.Add(r);
                    }
                }
            }
            context.SaveChanges();
        }
    }
}