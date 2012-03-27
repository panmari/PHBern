{ DOMINO.PAS }
{ Domino-Spiel mit Schildkroeten auf Papier }
{ Erstellt unter Omsi-Pascal am 4. Nov. 1985, A. Pluess, Uni Bern, KL }
{ Modifiziert unter Turbo-Pascal am 2. Aug. 1993, A. Pluess }

TYPE
   kartenbezeichner = RECORD
	    kn:0..9; { Kartennummer, 0:ungelegt }
	    kl:0..3  { Kartenlage }
	 END;
   brett = ARRAY[1..3,1..3] OF kartenbezeichner;

   figur = (gk,gs,kk,ks,pk,ps,vk,vs);
				   { Spiel auf Papier:
				     GestreiftKopf,GestreiftSchwanz,
				     KarriertKopf,KarriertSchwanz,
				     PunktiertKopf,PunktiertSchwanz,
				     VollKopf, VollSchwanz }

   karte = ARRAY[0..3] OF figur;
   spiel = ARRAY[1..9] OF karte;

   vorrat = ARRAY[1..9] OF BOOLEAN; { TRUE :  noch zu setzen,
				      FALSE:  bereits gesetzt }

{ ----------------------- Globale Variablen ---------------------------- }
VAR
   mitglied: spiel;
   gelegt  : brett;
   frisch  : vorrat;
   loesungszahl: INTEGER;

{ ----------------------- Procedure drucke ----------------------------- }
PROCEDURE drucke(gelegt: brett);
VAR
   zeile, spalte: 1..3;

BEGIN
   FOR zeile := 1 TO 3 DO
      BEGIN
	 FOR spalte := 1 TO 3 DO
	    WRITE('   Nr.:  ', gelegt[zeile,spalte].kn:3,
		  '   Lage: ', gelegt[zeile,spalte].kl:3);
	 WRITELN; WRITELN;
      END
END;
{ ======================= End of    drucke ============================= }

{ ----------------------- Procedure initmiglieder ---------------------- }
PROCEDURE initmitglieder;
{ Karten zeilenweise numeriert }
{ auf jeder Karte rechts beginnend, im Gegenuhrzeigersinn fortschreitend }

VAR
   kartennr: 1..9;

BEGIN
   kartennr := 1;
   mitglied[kartennr,0] := kk;
   mitglied[kartennr,1] := vs;
   mitglied[kartennr,2] := ks;
   mitglied[kartennr,3] := pk;
   kartennr := 2;
   mitglied[kartennr,0] := kk;
   mitglied[kartennr,1] := vk;
   mitglied[kartennr,2] := gs;
   mitglied[kartennr,3] := ps;
   kartennr := 3;
   mitglied[kartennr,0] := kk;
   mitglied[kartennr,1] := ps;
   mitglied[kartennr,2] := vs;
   mitglied[kartennr,3] := gk;
   kartennr := 4;
   mitglied[kartennr,0] := pk;
   mitglied[kartennr,1] := vk;
   mitglied[kartennr,2] := ks;
   mitglied[kartennr,3] := gs;
   kartennr := 5;
   mitglied[kartennr,0] := ks;
   mitglied[kartennr,1] := vk;
   mitglied[kartennr,2] := vk;
   mitglied[kartennr,3] := ps;
   kartennr := 6;
   mitglied[kartennr,0] := vs;
   mitglied[kartennr,1] := vk;
   mitglied[kartennr,2] := kk;
   mitglied[kartennr,3] := ps;
   kartennr := 7;
   mitglied[kartennr,0] := gk;
   mitglied[kartennr,1] := vk;
   mitglied[kartennr,2] := ps;
   mitglied[kartennr,3] := ks;
   kartennr := 8;
   mitglied[kartennr,0] := gk;
   mitglied[kartennr,1] := vk;
   mitglied[kartennr,2] := ps;
   mitglied[kartennr,3] := vs;
   kartennr := 9;
   mitglied[kartennr,0] := vs;
   mitglied[kartennr,1] := pk;
   mitglied[kartennr,2] := kk;
   mitglied[kartennr,3] := gs
END;
{ ======================= End of    initmiglieder ====================== }

{ ----------------------- Procedure initgelegt ------------------------- }
PROCEDURE initgelegt;
VAR
   zeile, spalte: 1..3;

BEGIN
   FOR zeile :=1 TO 3 DO
      FOR spalte :=1 TO 3 DO
	 BEGIN
	    gelegt[zeile,spalte].kn := 0; { alle ungelegt }
	    gelegt[zeile,spalte].kl := 0  { Anfangslage   }
	 END
END;

{ ======================= End of    initgelegt ========================= }

{ ----------------------- Procedure initfrisch ------------------------- }
PROCEDURE initfrisch;
VAR
   kartennr : 1..9;

BEGIN
   FOR kartennr := 1 TO 9 DO
      frisch[kartennr] := TRUE
END;
{ ======================= End of    initfrisch ------------------------- }

{ ----------------------- Procedure lege ------------------------------- }
PROCEDURE lege(gelegt: brett; frisch: vorrat);
VAR
   gefuellt: BOOLEAN;
   kartennummer, nr, ineu, kneu: 1..9;
   lage: 0..3;

{ --------------- Function passt -------------- }
FUNCTION passt(gelegt: brett; kartennr, lage: INTEGER): BOOLEAN;

{ ----- Function pruefe ----- }
FUNCTION pruefe (bild1, bild2:figur): BOOLEAN;
BEGIN
   CASE bild1 OF
      gk: pruefe := bild2 = gs;
      gs: pruefe := bild2 = gk;
      kk: pruefe := bild2 = ks;
      ks: pruefe := bild2 = kk;
      pk: pruefe := bild2 = ps;
      ps: pruefe := bild2 = pk;
      vk: pruefe := bild2 = vs;
      vs: pruefe := bild2 = vk
   END;
END;
{ ===== End of	 pruefe ===== }

{ ----- Function passtlinks - }
FUNCTION passtlinks: BOOLEAN;
VAR
   linksnr: 1..9;
   linkslage, index: 0..3;
   linksbild, frischbild: figur;

BEGIN
   IF kneu = 1
      THEN {am linken RANDE}
	 passtlinks := TRUE
      ELSE
	 BEGIN
	    linksnr    := gelegt[ineu,kneu-1].kn;
	    linkslage  := gelegt[ineu,kneu-1].kl;
	    index      := linkslage;
	    linksbild  := mitglied[linksnr,index];

	    index      := (lage+2) MOD 4;
	    frischbild := mitglied[kartennr,index];

	    passtlinks := pruefe(linksbild,frischbild)
	 END
END;
{ ===== End of	 passtlinks = }

{ ----- Function passtoben -- }
FUNCTION passtoben: BOOLEAN;
VAR
   obennr: 1..9;
   obenlage, index: 0..3;
   obenbild, frischbild: figur;

BEGIN
   IF ineu = 1
      THEN {am oberen RANDE}
	 passtoben := TRUE
      ELSE
	 BEGIN
	    obennr     := gelegt[ineu-1,kneu].kn;
	    obenlage   := gelegt[ineu-1,kneu].kl;
	    index      := (obenlage+3) MOD 4;
	    obenbild   := mitglied[obennr,index];

	    index      := (lage+1) MOD 4;
	    frischbild := mitglied[kartennr,index];

	    passtoben  :=pruefe(obenbild,frischbild)
	 END
END;
{ ===== End of	 passtoben == }

BEGIN
   passt := passtlinks AND passtoben
END;
{ =============== End of   passt ============== }

{ --------------- Procedure einfuegen --------- }
PROCEDURE einfuegen(kartennr, lage: INTEGER; VAR gelegt: brett);
BEGIN
   gelegt[ineu,kneu].kn := kartennr;
   gelegt[ineu,kneu].kl := lage
END;
{ =============== End of    einfuegen ========= }

{ --------------- Procedure rausnehmen -------- }
PROCEDURE rausnehmen(kartennr, lage: INTEGER; VAR gelegt: brett);
BEGIN
   gelegt[ineu,kneu].kn := 0;
   gelegt[ineu,kneu].kl := 0
END;
{ =============== End of    rausnehmen ======== }

BEGIN {of lege}

gefuellt := FALSE;
ineu := 1; kneu := 1;

IF gelegt[3,3].kn <> 0
   THEN gefuellt := TRUE {Brett gefuellt}
   ELSE
     { liefert ineu,kneu der 1. leeren Lage des Brettes }
      WHILE gelegt[ineu,kneu].kn <> 0 DO
	 IF kneu < 3
	    THEN
	       kneu := kneu+1
	    ELSE
	       BEGIN
		  ineu := ineu+1; kneu := 1
	       END;
   IF gefuellt
      THEN { Loesung gefunden }
	 BEGIN
	    WRITELN(loesungszahl:3, '.te Loesung gefunden:');
	    drucke(gelegt);
	    loesungszahl := loesungszahl+1
	 END
      ELSE
	 FOR kartennummer := 1 TO 9 DO
	    IF frisch[kartennummer] THEN { moegliche Karte }
	       FOR lage := 0 TO 3 DO { in allen Lagen }
		  IF passt(gelegt,kartennummer,lage) THEN { lege sie }
		     BEGIN
			einfuegen(kartennummer, lage, gelegt); { in Brett fuegen }
			frisch[kartennummer] := FALSE; { aus Vorrat heraus }

			lege(gelegt,frisch);  { rekursiver Aufruf }

			rausnehmen(kartennummer, lage, gelegt); { aus Brett weg }
			frisch[kartennummer] := TRUE { wieder in Vorrat }
		     END
END;
{ ======================= End of    lege ------------------------------- }

{ ----------------------- Hauptprogramm -------------------------------- }
BEGIN
   WRITELN('Moment mal, ich muss hirnen...');
   loesungszahl := 1;
   initmitglieder;
   initgelegt;
   initfrisch;
   lege(gelegt,frisch)
END.
