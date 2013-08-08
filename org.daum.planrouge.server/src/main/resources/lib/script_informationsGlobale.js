var id=0;
var ws={};
$(function () {
    $.toast.config.width = 600;
    $.toast.config.align = 'right';
    $.toast.config.closeForStickyOnly = false;

     ws = new ReconnectingWebSocket('ws://'+ document.location.host +'/getGlobalInformations');

    ws.onopen = function() {
        $.toast('<b>Success!</b> Connected with server : ws://' + document.location.host + '/getGlobalInformations', {type: 'success'});
        load();
    };
    ws.onclose = function() {
        $.toast('<b>Error!</b> Lost connection with server : ws://' + document.location.host + '/getGlobalInformations', {type: 'danger'});
    };
    ws.onmessage = function(msg) {
        var jsonObj = JSON.parse(msg.data);
        if( jsonObj.idIntervention != null && jsonObj.idIntervention!=''){
            var selectIntervention = document.getElementById("idIntervention");
            document.getElementById('idIntervention').options.length=0;
            selectIntervention.options[selectIntervention.length] = new Option("ID Intervention", "000");
            for ( var i in jsonObj.idIntervention) {
                selectIntervention.options[selectIntervention.length] = new Option(jsonObj.idIntervention[i], jsonObj.idIntervention[i]);
            }
        }
        if (jsonObj.donnees != null){
            var jsonObjDonnees = jsonObj.donnees;
            for (var i in jsonObjDonnees) {
                var jsonObj2 = jsonObjDonnees[i];
                var jIntervention = jsonObj2.intervention;
                if(jIntervention.id == id){
                    if(jsonObj2.type == "donnees"){
                        graph(jsonObj2);
                    }else{
                        $.toast('<b>Message! </b>'+msg.data, {type: 'info'});
                    }
                }
            }
        }
    };
});

function setId(){
    var idIntervention = document.getElementById("idIntervention");
    id = idIntervention.options[idIntervention.selectedIndex].value;
    ws.send("");
}

function getId(){
    ws.send("getid");
    id = $("#idIntervention").val();
}

function load(){
    ws.send("getid");
}

function selectIntervention(idInter){
    id = idInter;
    ws.send("");
}

// WINDOW SIZE
function adpaterALaTailleDeLaFenetre(){
    var largeur = document.documentElement.clientWidth;
    var source = document.getElementById('page');
    if (largeur < 1350){
        source.style.width = 700+'px';
    }else {
        source.style.width = 1350+'px';
    }
}

// Une fonction de compatibilité pour gérer les évènements
function addEvent(element, type, listener){
    if(element.addEventListener){
        element.addEventListener(type, listener, false);
    }else if(element.attachEvent){
        element.attachEvent("on"+type, listener);
    }
}

// On exécute la fonction une première fois au chargement de la page
addEvent(window, "load", adpaterALaTailleDeLaFenetre);
// Puis à chaque fois que la fenêtre est redimensionnée
addEvent(window, "resize", adpaterALaTailleDeLaFenetre);


// DELETE Rows of Table
function delRows(tableID) {
    myTBody = document.getElementById(tableID).getElementsByTagName('TBODY')[0];
    myTBody.innerHTML ='';
}

function graph(jsonObj){
    var jNbVictimeCat = jsonObj.nbVictimeCat;
    var jNbVictimeAge = jsonObj.nbVictimeAge;
    var jNbVictimeSexe = jsonObj.nbVictimeSexe;
    var arrayVictimes = jsonObj.victimes;
    var jIntervention = jsonObj.intervention;
    var nbAgents = jsonObj.agents;
    var nbVictime = jsonObj.nbVictime;



    // TABLE VICTIMS
    delRows("bodyTable");

    for ( var i in arrayVictimes) {
        var victime = arrayVictimes[i];
        var tableau = document.getElementById("bodyTable").getElementsByTagName('TBODY')[0];

        var ligne = tableau.insertRow(-1);

        var colonne1 = ligne.insertCell(0);
        colonne1.innerHTML =' ';
        if (victime.categorie != null){
            if (victime.categorie.code != null){
                colonne1.innerHTML += '<div class="cat'+victime.categorie.code+'">'+victime.categorie.code+'</div>';
            }
        }

        var colonne2 = ligne.insertCell(1);
        colonne2.innerHTML = ' ';
        if (victime.id != null){
            colonne2.innerHTML = victime.id;
        }

        var colonne3 = ligne.insertCell(2);
        colonne3.innerHTML = ' ';
        if (victime.nom != null){
            colonne3.innerHTML = victime.nom;
        }

        var colonne4 = ligne.insertCell(3);
        colonne4.innerHTML = ' ';
        if (victime.prenom != null){
            colonne4.innerHTML = victime.prenom ;
        }
        var colonne5 = ligne.insertCell(4);
        colonne5.innerHTML =  ' ';
        if (victime.age != null){
            if (victime.age == '0'){
               colonne5.innerHTML='--';
            }else{
            colonne5.innerHTML = victime.age;
            }
        }

        var colonne6 = ligne.insertCell(5);
        colonne6.innerHTML = '--';
        if (victime.dateNaissance != null){
            if(victime.dateNaissance =='00000000'){
                colonne6.innerHTML = '--';
            }else{
               colonne6.innerHTML = victime.dateNaissance;
            }

        }

        var colonne7 = ligne.insertCell(6);
        colonne7.innerHTML = '--';
        if (victime.sexe != null){
            if( victime.sexe == 1) {
                colonne7.innerHTML = 'Homme';
            }else if (victime.sexe == 2){
                colonne7.innerHTML = 'Femme';
            }
        }
    }


    // NUMERO Intervention
    document.getElementById('nIntervention').innerHTML = jIntervention.id;


    // NB VICTIMS
    document.getElementById('nbVictime').innerHTML = nbVictime;

    //NB VICTIMS SEX
    document.getElementById('nbHommes').innerHTML = jNbVictimeSexe[0];
    document.getElementById('nbFemmes').innerHTML = jNbVictimeSexe[1];


    // NB AGENTS
    document.getElementById('nbAgents').innerHTML = nbAgents;

    // INTERVENTION
    var description ;
    if(jIntervention.description != null){
        description = '<div><b>Description : </b>'+jIntervention.description+'</div>';
    }
    if(jIntervention.position != null){
        if(jIntervention.position.nomVille != null){
            description += '<div><b>Ville : </b>'+jIntervention.position.nomVille+'</div>';
        }
        if(jIntervention.position.cp != null){
            description += '<div><b>Code Postal : </b>'+jIntervention.position.cp+'</div>';
        }
    }
    document.getElementById('description').innerHTML = description ;


    // BAR CHART

    var max = Math.max(jNbVictimeCat[0], Math.max(jNbVictimeCat[1], Math.max(jNbVictimeCat[2],
    Math.max(jNbVictimeCat[3], jNbVictimeCat[4]))));

    var barChartData = {
        labels : ["U3","U2","U1","EU","DCD"],
        datasets : [
            {
                fillColor : "rgba(41,128,185,1)",
                strokeColor : "rgba(41,128,185,1)",

                data : [ parseInt(jNbVictimeCat[0]), parseInt(jNbVictimeCat[1]), parseInt(jNbVictimeCat[2]),
                parseInt(jNbVictimeCat[3]), parseInt(jNbVictimeCat[4])]
            }
        ]
    };

    var option = { //Boolean - If we want to override with a hard coded scale
        scaleOverride : true,
        //** Required if scaleOverride is true **
        //Number - The number of steps in a hard coded scale
        scaleSteps : parseInt(max),
        //Number - The value jump in the hard coded scale
        scaleStepWidth : 1,
        //Number - The scale starting value
        scaleStartValue : 0,
        scaleLineColor : "rgba(0,0,0,1)",
        animation : false,
    }

    var myLine = new Chart(document.getElementById("canvas").getContext("2d")).Bar(barChartData,option);


    // BAR CHART AGE

    var maxAge = Math.max(jNbVictimeAge[0], Math.max(jNbVictimeAge[1], Math.max(jNbVictimeAge[2],
    Math.max(jNbVictimeAge[3], Math.max(jNbVictimeAge[4], Math.max(jNbVictimeAge[5], Math.max(jNbVictimeAge[6],
    jNbVictimeAge[7])))))));
    var barChartDataAge = {
        labels : ["0-5","6-10","11-20","21-30","31-40","41-60","61-80","80+"],
        datasets : [
            {
                fillColor : "rgba(22,160,133,1)",
                strokeColor : "rgba(22,160,133,1)",

                data : [ parseInt(jNbVictimeAge[0]), parseInt(jNbVictimeAge[1]), parseInt(jNbVictimeAge[2]),
                parseInt(jNbVictimeAge[3]),
                parseInt(jNbVictimeAge[4]),parseInt(jNbVictimeAge[5]),parseInt(jNbVictimeAge[6]),parseInt(jNbVictimeAge[7])]
            }

        ]
    };

    var optionAge = { //Boolean - If we want to override with a hard coded scale
        scaleOverride : true,

        //** Required if scaleOverride is true **
        //Number - The number of steps in a hard coded scale
        scaleSteps : parseInt(maxAge),
        //Number - The value jump in the hard coded scale
        scaleStepWidth : 1,
        //Number - The scale starting value
        scaleStartValue : 0,
        scaleLineColor : "rgba(0,0,0,1)",
        animation : false,
    }
    var myLineAge = new Chart(document.getElementById("canvasAge").getContext("2d")).Bar(barChartDataAge,optionAge);
}