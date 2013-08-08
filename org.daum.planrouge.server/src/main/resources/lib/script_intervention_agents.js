    /* Global variables */
    var ws = {};
    var wsGet = {};
    var tabIntervention = new Object();


    $(function () {
         $.toast.config.width = 600;
         $.toast.config.align = 'right';
         $.toast.config.closeForStickyOnly = false;


        // Client WebServer ADD
        ws = new ReconnectingWebSocket('ws://' + document.location.host + '/add');

        ws.onopen = function (msg) {
            $.toast('<b>Success!</b> Connected with server : ws://' + document.location.host + '/add', {type: 'success'});
        };
        ws.onclose /*= ws.onerror*/ = function (msg) {
            $.toast('<b>Error!</b> Lost connection with server : ws://' + document.location.host + '/add', {type: 'danger'});
        };
        ws.onmessage = function (evt) {
            var data = evt.data;
        };

        // Client WebServer DELETE
        wsDelete = new ReconnectingWebSocket('ws://' + document.location.host + '/delete');

        wsDelete.onopen = function (msg) {
            $.toast('<b>Success!</b> Connected with server : ws://' + document.location.host + '/delete', {type: 'success'});
        };
        wsDelete.onclose /*= ws.onerror*/ = function (msg) {
            $.toast('<b>Error!</b> Lost connection with server : ws://' + document.location.host + '/delete', {type: 'danger'});
        };
        wsDelete.onmessage = function (evt) {
            var data = evt.data;
            // $.toast('<b>Message! </b>'+data, {type: 'info'});
        };

        // Client WebServer GETALL
        wsGet = new ReconnectingWebSocket('ws://' + document.location.host + '/getAll');

        wsGet.onopen = function (msg) {
            $.toast('<b>Success!</b> Connected with server : ws://' + document.location.host + '/getAll', {type: 'success'});
            $("#submitGetAllInterventions").get(0).click();
        };
        wsGet.onclose /*= ws.onerror*/ = function (msg) {
            $.toast('<b>Error!</b> Lost connection with server : ws://' + document.location.host + '/getAll', {type: 'danger'});
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
                }
            }
        };



        $(".addAgentIntervention").click(function () {
            var agent = new Object();
            agent.type = "AdapterAgent";
            wsGet.send(JSON.stringify(agent));
            $("#submitGetAllInterventions").get(0).click();
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
               $("#submitGetAllInterventions").get(0).click();
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
              $("#submitGetAllInterventions").get(0).click();
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
              $("#submitGetAllInterventions").get(0).click();
        });


        $("#submitCreateIntervention").click(function () {


            var description = jQuery("#descriptionInput").get(0).value;
            var ville = jQuery("#villeInput").get(0).value;
            var cp = jQuery("#cpInput").get(0).value;
            var rue = jQuery("#NomRueInput").get(0).value;

            var position = new Object();
            position.nomVille = ville;
            position.nomRue = rue;
            position.cp = cp;
            position.type = "AdapterPositionCivile";

            var intervention = new Object();
            intervention.type = "AdapterIntervention";
            intervention.description = description;
            intervention.position = position;


            ws.send(JSON.stringify(intervention));
            $('#add-modal').modal('hide');
           $("#submitGetAllInterventions").get(0).click();
        }) ;

        $("#submitGetAllAgents").click(function () {

            var agent = new Object();
            agent.type = "AdapterAgent";
            wsGet.send(JSON.stringify(agent));

            $("#submitGetAllInterventions").get(0).click();
        }) ;

        $("#submitGetAllInterventions").click(function () {
            var intervention = new Object();
            intervention.type = "AdapterIntervention";

            wsGet.send(JSON.stringify(intervention));


        }) ;

        $("#submitCreateAgent").click(function () {
            var matricule = jQuery("#MatriculeInput").get(0).value;

            var agent = new Object();
            agent.matricule = matricule;
            agent.type = "AdapterAgent";

            ws.send(JSON.stringify(agent));
            $('#add-modal-agent').modal('hide');
            $("#submitGetAllInterventions").get(0).click();
        }) ;

         $("#submitDeleteAgent").click(function () {
             $('#deleteAgent input:checked').each(function() {
                 var newAgent = new  Object();
                 newAgent.type = 'AdapterAgent';
                 newAgent.matricule =  $(this).attr('value');
                  alert(JSON.stringify(newAgent));
                 wsDelete.send(JSON.stringify(newAgent));
             });
             $('#delete-modal-agent').modal('hide');
             $("#submitGetAllInterventions").get(0).click();
              $("#submitGetAllAgents").get(0).click();
         });

         $("#btnDeleteAgent").click(function () {
            getAllAgents();
            $("#submitGetAllInterventions").get(0).click();
         });

    });

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

        $("#submitGetAllInterventions").get(0).click();
    }

    function getAllAgents() {
        var agent = new Object();
        agent.type = "AdapterAgent";
        wsGet.send(JSON.stringify(agent));
        $("#submitGetAllInterventions").get(0).click();
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
        $("#submitGetAllInterventions").get(0).click();
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




