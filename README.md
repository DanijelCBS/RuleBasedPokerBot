# RuleBasedPokerBot

## Motivacija
Poker, odnosno igra kartama, je “igra nesavršenog znanja”, u kojoj učestvuje više takmičara, koji moraju razumeti procenu, predviđanje, upravljanje rizikom i čitanje protivničke igre, samim tim, predstavlja dobar primer sistema, u kome je potrebno doneti odluke u uslovima nesigurnosti i sistema, koji bi se mogao modelovati određenim pravilima.

## Pregled problema
Problem, koji se rešava ovim projektom, predstavlja implementaciju bota, koji bi se mogao koristiti za igranje pokera. Njegovo ponašanje u različitim situacijama, u kojima može da se nađe tokom igre, bilo bi modelovano pravilima, zasnovanim na ekspertskom znanju. Tokom prethodnih dvadeset godina, pravljeni su poker botovi primenjujući razne metode, kao što su stabla igre, Monte Karlo simulacija, duboke neuronske mreže itd. Konkretno, performanse ovog bota bi se testirale pomoću PokerAcademy softvera, koji predstavlja platformu, na kojoj poker botovi mogu da igraju partije između sebe (https://code.google.com/archive/p/opentestbed/source/default/source), a kao literaturu bih koristio master rad sa Univerziteta u Alberti, na temu Dealing with Imperfect Information in Poker (http://poker.cs.ualberta.ca/publications/papp.msc.pdf).

## Metodologija rada
Ulazi: Vrednosti ulaza variraju na početku, a i u toku svake „ruke“ (partije) tačnije pri svakom menjanju faze jedne partije. U ulaze spadaju faza igre (pre-flop, flop, turn, river), broj igrača, pozicija bota za stolom, hole cards (karte koje je bot dobio), board (karte koje su izašle na teren), pot (suma novca ili čipova, koja se nalazi u igri), bet/raise (suma novca ili čipova, koju bi bot trebalo da plati, ukoliko želi da nastavi igru), blind (suma koju na početku partije moraju da plate dva igrača, zavisno od pozicije za stolom). Na osnovu ovih ulaza, bi se računale razne heuristike, koje bi se dalje koristile u pravilima.
