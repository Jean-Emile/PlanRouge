    /* Global variables */
    var ws = {};
    var wsGet = {};
    var tabIntervention = new Object();

var address = document.location.host;
address= address.substring(0,address.length-4)+'8080';

    $(function () {
         $.toast.config.width = 600;
         $.toast.config.align = 'right';
         $.toast.config.closeForStickyOnly = false;

        // Client WebServer ADD
        ws = new ReconnectingWebSocket('ws://' + address + '/add');

        ws.onopen = function (msg) {
            $.toast('<b>Success!</b> Connected with server : ws://' + address + '/add', {type: 'success'});
        };
        ws.onclose /*= ws.onerror*/ = function (msg) {
            $.toast('<b>Error!</b> Lost connection with server : ws://' + address + '/add', {type: 'danger'});
        };
        ws.onmessage = function (evt) {
            var data = evt.data;
        };

        // Client WebServer DELETE
        wsDelete = new ReconnectingWebSocket('ws://' + address + '/delete');

        wsDelete.onopen = function (msg) {
            $.toast('<b>Success!</b> Connected with server : ws://' + address + '/delete', {type: 'success'});
        };
        wsDelete.onclose /*= ws.onerror*/ = function (msg) {
            $.toast('<b>Error!</b> Lost connection with server : ws://' + address + '/delete', {type: 'danger'});
        };
        wsDelete.onmessage = function (evt) {
            var data = evt.data;
            // $.toast('<b>Message! </b>'+data, {type: 'info'});
        };

        // Client WebServer GETALL
        wsGet = new ReconnectingWebSocket('ws://' + address+ '/getAll');

        wsGet.onopen = function (msg) {
            $.toast('<b>Success!</b> Connected with server : ws://' + address + '/getAll', {type: 'success'});
            submitGetAllInterventions();
        };
        wsGet.onclose /*= ws.onerror*/ = function (msg) {
            $.toast('<b>Error!</b> Lost connection with server : ws://' + address + '/getAll', {type: 'danger'});
        };
        wsGet.onmessage = function (evt) {
            var data = evt.data;
            var jsonObject = JSON.parse(data);

            if(jsonObject.type == 'AdapterAgent'){

                var addAgent = document.getElementById("addAgent");
                var deleteAgent = document.getElementById("deleteAgent");
                addAgent.innerHTML = '';
                deleteAgent.innerHTML = '';
                for ( var i in jsonObject.arrayAgents) {
                    var nodeAdd = document.createElement('div');
                    nodeAdd.innerHTML = '<input type="checkbox" id="checkAdd'+ i + '" name="checkAdd' + i + '" value="'+jsonObject.arrayAgents[i].matricule+'"><label for="checkAdd' + i + '">'+ jsonObject.arrayAgents[i].matricule +'</label>';
                    var nodeDel = document.createElement('div');
                    nodeDel.innerHTML = '<input type="checkbox" id="checkDel'+ i + '" name="checkDel' + i + '" value="'+jsonObject.arrayAgents[i].matricule+'"><label for="checkDel' + i + '">'+ jsonObject.arrayAgents[i].matricule +'</label>';

                    addAgent.appendChild(nodeAdd);
                    deleteAgent.appendChild(nodeDel);
                }
            } else if(jsonObject.type == 'AdapterIntervention'){

                delRows('tableauInterventions');
                for ( var i in jsonObject.arrayInterventions) {
                    tabIntervention[jsonObject.arrayInterventions[i].id] = JSON.stringify(jsonObject.arrayInterventions[i]);

                    var tableau = document.getElementById("tableauInterventions");

                    var ligne = tableau.insertRow(-1);

                    var colonne1 = ligne.insertCell(0);
                    colonne1.innerHTML += jsonObject.arrayInterventions[i].id;

                    var colonne2 = ligne.insertCell(1);
                    colonne2.innerHTML = 'indéfini';
                    if( jsonObject.arrayInterventions[i].position != null){
                        if(jsonObject.arrayInterventions[i].position.nomVille != null){

                            colonne2.innerHTML = jsonObject.arrayInterventions[i].position.nomVille;
                        }
                    }
                    var colonne3 = ligne.insertCell(2);
                    colonne3.innerHTML += jsonObject.arrayInterventions[i].description;

                    var colonne4 = ligne.insertCell(3);
                    colonne4.innerHTML += '<a class="btn btn-primary" onclick="addAgentIntervention('+jsonObject.arrayInterventions[i].id+');" data-toggle="modal" href="#add-modal-agent-intervention" title="Create Agent ">Ajouter des Agents</a>';

                    var colonne5 = ligne.insertCell(4);
                    colonne5.innerHTML += '<a class="btn btn-primary" onclick="delAgentIntervention('+jsonObject.arrayInterventions[i].id+');" data-toggle="modal" href="#del-modal-agent-intervention" title="Delete Agent ">Supprimer des Agents</a>';

                    var colonne6 = ligne.insertCell(5);
                    colonne6.innerHTML += '<a class="btn btn-primary" onclick="editIntervention('+jsonObject.arrayInterventions[i].id+');" data-toggle="modal" href="#edit-modal-intervention" title="Edit intervention ">Modifier l\'intervention</a>';

                    var colonne7 = ligne.insertCell(6);
                    colonne7.innerHTML += '<a class="btn btn-primary" onclick="deleteIntervention('+jsonObject.arrayInterventions[i].id+');" data-toggle="modal" title="Delete intervention ">Supprimer l\'intervention</a>';
                }
            }
        };


         // ADD AGENT
        $(".addAgentIntervention").click(function () {
            var agent = new Object();
            agent.type = "AdapterAgent";
            wsGet.send(JSON.stringify(agent));
            submitGetAllInterventions();
        }) ;

        // UPDATE INTERVENTION
        $("#edit_submitCreateIntervention").click(function () {
             var description = jQuery("#edit_descriptionInput").get(0).value;
             var ville = jQuery("#edit_villeInput").get(0).value;
             var cp = jQuery("#edit_cpInput").get(0).value;
             var rue = jQuery("#edit_NomRueInput").get(0).value;
             var id = jQuery("#edit_idIntervention").text();
             var position;
             var intervention = tabIntervention[id];

             var jsonIntervention = JSON.parse(intervention);
             if(jsonIntervention.position != null){
                 position = jsonIntervention.position;
             } else {
                position = new Object();
             }

              position.nomVille = ville;
              position.nomRue = rue;
              position.cp = cp;
              position.type = "AdapterPositionCivile";


               jsonIntervention.type = "AdapterIntervention";
               jsonIntervention.description = description;
               jsonIntervention.id = id;
               jsonIntervention.position = position;

                ws.send(JSON.stringify(jsonIntervention));
                $('#edit-modal-intervention').modal('hide');
                submitGetAllInterventions();
        });

        $("#submitAddAgentToIntervention").click(function () {
            var id = jQuery("#idIntervention").text();
            var intervention = tabIntervention[id];
            var jsonIntervention = JSON.parse(intervention);
            var arrayAgents = jsonIntervention.agents;

            $('#addAgent input:checked').each(function() {
                var newAgent = new  Object();
                newAgent.type = 'AdapterAgent';
                newAgent.matricule =  $(this).attr('value')

                arrayAgents.push(newAgent);
            });
             jsonIntervention.agents = arrayAgents;

             ws.send(JSON.stringify(jsonIntervention));
              $('#add-modal-agent-intervention').modal('hide');
              submitGetAllInterventions();
        });

        $("#submitDelAgentToIntervention").click(function () {
            var id = jQuery("#idIntervention_delete").text();
            var intervention = tabIntervention[id];
            var jsonIntervention = JSON.parse(intervention);
            var arrayAgents = [];

            $('#delAgent input:not(:checked)').each(function() {
                var newAgent = new  Object();
                newAgent.type = 'AdapterAgent';
                newAgent.matricule =  $(this).attr('value')

                arrayAgents.push(newAgent);
            });
             jsonIntervention.agents = arrayAgents;

             ws.send(JSON.stringify(jsonIntervention));
              $('#del-modal-agent-intervention').modal('hide');
              submitGetAllInterventions();
        });


        $("#submitCreateIntervention").click(function () {
               document.getElementById("descriptionError").innerHTML='';
               document.getElementById("villeError").innerHTML= '';
               document.getElementById("cpError").innerHTML=  '';
               document.getElementById("rueError").innerHTML=  '';
               document.getElementById("rueError").className ='';
               document.getElementById("descriptionError").className ='';
               document.getElementById("cpError").className ='';
               document.getElementById("villeError").className ='';

            var description = jQuery("#descriptionInput").get(0).value;
            var ville = jQuery("#villeInput").get(0).value;
            var cp = jQuery("#cpInput").get(0).value;
            var rue = jQuery("#NomRueInput").get(0).value;

            if (description !='' && ville !='' && cp!='' && rue!=''){


                var position = new Object();
                position.nomVille = ville;
                position.nomRue = rue;
                position.cp = cp;
                position.type = "AdapterPositionCivile";
                position.heure = new Date().getTime();

                var intervention = new Object();
                intervention.type = "AdapterIntervention";
                intervention.description = description;
                intervention.position = position;


                ws.send(JSON.stringify(intervention));

                submitGetAllInterventions();
                $('#add-modal').modal('hide');
                $.toast('Intervention créée', {type: 'info'});
           }else{
                if(description ==''){
                  $.toast('Veuillez remplir la DESCRIPTION', {type: 'danger'});
                   document.getElementById("descriptionError").innerHTML="Indiquer la DESCRIPTION";
                   document.getElementById("descriptionError").className ='error';

                }
                if(ville ==''){
                   $.toast('Veuillez remplir la VILLE', {type: 'danger'});
                    document.getElementById("villeError").innerHTML="Indiquer la VILLE";
                    document.getElementById("villeError").className ='error';
                }
                if(cp ==''){
                  $.toast('Veuillez remplir le CODE POSTAL', {type: 'danger'});
                  document.getElementById("cpError").innerHTML="Indiquer le CODE POSTAL";
                  document.getElementById("cpError").className ='error';
                }
                 if(rue ==''){
                  $.toast('Veuillez remplir la RUE', {type: 'danger'});
                  document.getElementById("rueError").innerHTML="Indiquer la RUE";
                  document.getElementById("rueError").className ='error';
                }

           }

        }) ;

//        $("#submitGetAllAgents").click(function () {
//
//            var agent = new Object();
//            agent.type = "AdapterAgent";
//            wsGet.send(JSON.stringify(agent));
//            submitGetAllInterventions();
//        }) ;

   //



        $("#submitCreateAgent").click(function () {
            var matricule = jQuery("#MatriculeInput").get(0).value;
            document.getElementById("matriculeError").innerHTML=  '';
            document.getElementById("matriculeError").className ='';

            if (matricule !=''){
                 var agent = new Object();
                 agent.matricule = matricule;
                 agent.type = "AdapterAgent";

                 ws.send(JSON.stringify(agent));
                 $('#add-modal-agent').modal('hide');
               submitGetAllInterventions();
            }else {
            document.getElementById("matriculeError").innerHTML=  'Indiquer le MATRICULE';
            document.getElementById("matriculeError").className ='error';
                 $.toast('Veuillez remplir le MATRICULE', {type: 'danger'});
            }

        }) ;

        // DELETE AGENT
         $("#submitDeleteAgent").click(function () {
             $('#deleteAgent input:checked').each(function() {
                 var agent = new  Object();
                 agent.type = 'AdapterAgent';
                 agent.matricule =  $(this).attr('value');
                 wsDelete.send(JSON.stringify(agent));
             });
             $('#delete-modal-agent').modal('hide');

              submitGetAllInterventions();
//              $("#submitGetAllAgents").get(0).click();
         });

         $("#btnDeleteAgent").click(function () {
            getAllAgents();

             submitGetAllInterventions();
         });



    });
        function submitGetAllInterventions(){
            var intervention = new Object();
            intervention.type = "AdapterIntervention";
            wsGet.send(JSON.stringify(intervention));
        }
function deleteIntervention(id){
     var intervention =  tabIntervention[id];

      wsDelete.send(intervention);
      submitGetAllInterventions();
}
     // DELETE Rows of Table
    function delRows(tableID) {
        myTBody = document.getElementById(tableID).getElementsByTagName('TBODY')[0];
        while (myTBody.getElementsByTagName('TR').length > 1) {
            myTBody.deleteRow(1);
        }
    }
      // ADD AGENT TO INTERVENTION
    function addAgentIntervention (id){
        $("#idIntervention").text(id);
        getAllAgents();
        submitGetAllInterventions();
    }

    function getAllAgents() {
        var agent = new Object();
        agent.type = "AdapterAgent";
        wsGet.send(JSON.stringify(agent));
        submitGetAllInterventions();
    }

       //DELETE AGENT OF INTERVENTION
    function delAgentIntervention (id){
        var json = tabIntervention[id];
        var jsonObject = JSON.parse(json);
        var delAgent = document.getElementById("delAgent");
        delAgent.innerHTML = '';
        for(var i in jsonObject.agents){
            var node = document.createElement('div');
            node.innerHTML = '<input type="checkbox" id="delete_check_'+id+'_' + i + '" name="delete_check_'+id+'_' + i + '"value ="'+jsonObject.agents[i].matricule +'"><label for="delete_check_'+id+'_' + i + '">'+ jsonObject.agents[i].matricule +'</label>';
            delAgent.appendChild(node);
        }
        $("#idIntervention_delete").text(jsonObject.id);
        submitGetAllInterventions();
    }

        // EDIT INTERVENTION
    function editIntervention (id){
        var json = tabIntervention[id];
        var jsonObject = JSON.parse(json);
        var ville = '';
        var cp = '';
        var rue = '';
        var description ='';

        description = jsonObject.description;

        if(jsonObject.position !=null){
            if ( jsonObject.position.nomVille !=null){
                ville = jsonObject.position.nomVille;
            }
            if ( jsonObject.position.cp !=null){
                cp = jsonObject.position.cp;
            }
            if ( jsonObject.position.nomVille !=null){
               rue = jsonObject.position.nomRue;
            }
        }

        $("#edit_cpInput").val(cp);
        $("#edit_descriptionInput").val(description);
        $("#edit_villeInput").val(ville);
        $("#edit_NomRueInput").val(rue);
        $("#edit_idIntervention").text(id);
    }




