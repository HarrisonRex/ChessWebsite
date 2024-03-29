function pageLoad() {
    checkLogin();
    resetNewFriendForm();
    loadNewGameOpoDropDown();
    loadPendingFriendsList();
    loadFriendsList();
    loadGames();
    $("#gameMaker").hide();
    $("#newFriend").hide();
}

//Checks the login user when they load the page, checks if their cookie is still valid
function checkLogin() {

    let token = Cookies.get("sessionToken");

    if (token === undefined) {
        window.location.href = "/client/login.html";
    } else {
        $.ajax({
            url: '/user/get',
            type: 'GET',
            success: username => {
                if (username === "") {
                    window.location.href = "/client/login.html";
                } else {
                    $("#username").html(username);
                    $("#userRecon").html("You are logged in as " + username);
                }
            }
        });
    }

    $("#logout").click(event => {
        Cookies.remove("sessionToken");
        window.location.href = "/client/login.html";
    });
}

//Sudo test function, will set a new chessboard
function boardSetChess(){
    //To use for columns
    let Chartas=["I don't exist","A","B","C","D","E","F","G","H"];

    //Pawns
    for(let i=1; i<=8;i++){
        //White
        let BSP = Chartas[i]+"2";
        $('#'+BSP).html("&#9817;");
    }for(let i=1; i<=8;i++) {
        //Black
        let BSP = Chartas[i] + "7";
        $('#' + BSP).html("&#9823;");
    }

    //White Pieces
    for(let i=1; i<=8;i++) {
        let BSP = Chartas[i] + "1";
        if (i===1||i===8){
            //Rook
            $('#' + BSP).html("&#9814;");
        }else if(i===2||i===7){
            //Knight
            $('#' + BSP).html("&#9816;");
        }else if(i===3||i===6){
            //Bishop
            $('#' + BSP).html("&#9815;");
        }else if(i===4){
            //Queen
            $('#' + BSP).html("&#9813;");
        }else if(i===5){
            //King
            $('#' + BSP).html("&#9812;");
        }
    }

    //Black Pieces
    for(let i=1; i<=8;i++) {
        let BSP = Chartas[i] + "8";
        if (i===1||i===8){
            //Rook
            $('#' + BSP).html("&#9820;");
        }else if(i===2||i===7){
            //Knight
            $('#' + BSP).html("&#9822;");
        }else if(i===3||i===6){
            //Bishop
            $('#' + BSP).html("&#9821;");
        }else if(i===4){
            //Queen
            $('#' + BSP).html("&#9819;");
        }else if(i===5){
            //King
            $('#' + BSP).html("&#9818;");
        }
    }

    //Clear Spaces
    for(let i=1; i<=8;i++) {
        for(let c=3;c<=6;c++) {
            let BSP = Chartas[i] + c;
            $('#' + BSP).html("");
        }
    }
}

//Adds a new game to the database
function resetNewGameForm(){
    const CreateGameForm = $('#newGameForm');
    CreateGameForm.submit(event => {
        event.preventDefault();
    $.ajax({
        url: '/games/new',
        type: 'POST',
        data: CreateGameForm.serialize(),
        success: response => {
            if(response.startsWith('Error:')){
                alert(response);
            }else{
                $("#gameMaker").hide();
                pageLoad();
                selectGameRadioButtonChecker("gameSelectRad", game.gameId);
            }
        }
        })
    })
}

function resetNewFriendForm() {
    const CreateNewFriend = $('#newFriendForm');
    CreateNewFriend.submit(event => {
        event.preventDefault();
        $.ajax({
            url: '/friendsList/new',
            type: 'POST',
            data: CreateNewFriend.serialize(),
            success: response => {
                if (response.startsWith('Error:')) {
                    alert(response);
                } else {
                    $("#newFriend").hide();
                }
            }
        })
    })
}

function loadPendingFriendsList() {
    let pendingFriendsListHTML = '';
    $.ajax({
        url: '/friendsList/pendingList',
        type: 'GET',
        success: pendingFriendsListList => {
            if(pendingFriendsListList.hasOwnProperty('error')) {
                alert(pendingFriendsListList.error);
            }else{
                if (!pendingFriendsListList.hasOwnProperty('empty')) {
                    pendingFriendsListHTML = "Pending Friend Requests:"
                    for (let pendingFriend of pendingFriendsListList) {
                        pendingFriendsListHTML += renderPendingFriend(pendingFriend);
                    }
                }
            }
            $('#pendingFriendsShower').html(pendingFriendsListHTML);
            resetDeletePendingFriend();
            resetAcceptPendingFriend();
        }
        })
}

function renderPendingFriend(pendingFriend) {
    return `<div>` +
        `<div class="pendingFriendName" id="name${pendingFriend.friendsListId}">${pendingFriend.user1UN} ` +
        `<button class="acceptPendingFriend" data-pendingFriend-friendListId="${pendingFriend.friendsListId}">` +
        `Accept` +
        `</button>` +
        `<button class="deletePendingFriend" data-pendingFriend-friendListId="${pendingFriend.friendsListId}">` +
        `Reject` +
        `</button>` +
        `</div>` +
        `</div>`;
}

function loadFriendsList() {
    let friendsListHTML = '';
    $.ajax({
        url: '/friendsList/list',
        type: 'GET',
        success: friendsListList => {
            if(friendsListList.hasOwnProperty('error')) {
                alert(friendsListList.error);
            }else{
                if(!friendsListList.hasOwnProperty('empty')) {
                    friendsListHTML += "Friends:"
                    for (let friend of friendsListList) {
                        friendsListHTML += renderFriend(friend);
                    }
                }
            }
            $('#FriendsShower').html(friendsListHTML);
            resetDeleteFriend();
        }
    })
}

function renderFriend(friend) {
    return `<div>` +
        `<div class="friendName" id="name${friend.friendsListId}">${friend.otherUser} ` +
        `<button class="deleteFriend" data-friend-friendListId="${friend.friendsListId}">` +
        `Delete Friend` +
        `</button>` +
        `</div>` +
        `</div>`;
}

function resetDeletePendingFriend() {
    $('.deletePendingFriend').click(event => {
        const pendingFriendId = $(event.target).attr('data-pendingFriend-friendListId');
        $.ajax({
            url: '/friendsList/pendingDelete',
            type: 'POST',
            data: {"pendingFriendId": pendingFriendId},
            success: response => {
                if (response === 'OK') {
                    pageLoad();
                } else {
                    alert(response);
                }
            }
        });
    });
}

function resetDeleteFriend() {
    $('.deleteFriend').click(event => {
        const friendId = $(event.target).attr('data-friend-friendListId');
        $.ajax({
            url: '/friendsList/delete',
            type: 'POST',
            data: {"friendId": friendId},
            success: response => {
                if (response === 'OK') {
                    pageLoad();
                } else {
                    alert(response);
                }
            }
        });
    });
}

function resetAcceptPendingFriend() {
    $('.acceptPendingFriend').click(event => {
        const pendingFriendId = $(event.target).attr('data-pendingFriend-friendListId');
        $.ajax({
            url: '/friendsList/pendingAccept',
            type: 'POST',
            data: {"pendingFriendId": pendingFriendId},
            success: response => {
                if (response === 'OK') {
                    pageLoad();
                } else {
                    alert(response);
                }
            }
        });
    });
}

function loadNewGameOpoDropDown() {
    let newGameOpoHTML = '';
    $.ajax({
        url: '/friendsList/loadOpoDD',
        type: 'GET',
        success: newGameOpoList => {
            if(newGameOpoList.hasOwnProperty('error')) {
                alert(newGameOpoList.error);
            }else{
                for (let opo of newGameOpoList) {
                    newGameOpoHTML += renderNewGameOpoDropDownOption(opo);
                }
            }
            $('#newGameOpoDropDown').html(newGameOpoHTML);
            resetNewGameForm();
        }
    })
}

function renderNewGameOpoDropDownOption(opoOpt) {
    return `<option class="newGameOpoOption" id="opo${opoOpt.friendsListid}" value="${opoOpt.otherUser}">${opoOpt.otherUser}` +
        `</option>`;
}

function loadGames() {
    let showGamesHTML ='';
    let currentSelect = getGameRadioId();
    $.ajax({
        url: '/games/list',
        type: 'GET',
        success: showGamesList => {
            if(showGamesList.hasOwnProperty('error')) {
                alert(showGamesList.error);
            }else{
                let colour = "";

                for (let game of showGamesList) {

                    if(game.ownerWhite===1){
                        colour = "White";
                    }else{
                        colour = "Black";
                    }

                    showGamesHTML += renderCurrentGameRadio(game, colour);
                }
            }
            $('#GamesShower').html(showGamesHTML);
            resetDeleteGameRadio();

            for (let game of showGamesList){
                if(game.gameId==currentSelect){
                    selectGameRadioButtonChecker("gameSelectRad", game.gameId);
                }
            }
        }
    })
}

function renderCurrentGameRadio(game, colour) {
    return `<div>` +
        `<input type="radio" name="gameSelectRad" value="${game.gameId}" class="gameOpoName" onchange="renderSavedGameSelected()" id="name${game.gameId}">` +
        `Against: ` + game.otherPlayer +
        `, you are playing as ` + colour +
        `, game id:` + game.gameId +
        `</input> ` +
        `<button class="deleteGameRadio" data-game-gameId="${game.gameId}">` +
        `Delete Game` +
        `</button>` +
        `</div>`;
}

function resetDeleteGameRadio() {
    $('.deleteGameRadio').click(event => {
        const gameId = $(event.target).attr('data-game-gameId');
        $.ajax({
            url: '/games/delete',
            type: 'POST',
            data: {"gameId": gameId},
            success: response => {
                if (response === 'OK') {
                    pageLoad();
                } else {
                    alert(response);
                }
            }
        })
    })
}

function getGameRadioId() {
    return $("input[name='gameSelectRad']:checked").val();
}

function selectGameRadioButtonChecker(name, value) {
    $("input[name='"+name+"'][value='"+value+"']").prop('checked', true);
    return false;
}

function renderSavedGameSelected() {
    //alert("Im a test for " + getGameRadioId());
    boardSetChess();
    resetSetSelectedGame(getGameRadioId());
}

function resetSetSelectedGame(selected) {
    let gInfo = "This game is: \n \n";
    let gData = "";
    let lastPref = 0;
    $.ajax({
        url: '/games/getOne',
        type: 'POST',
        data: {"gameId": selected},
        success: response => {
            if (response == "Error: This game doesn't exist"){
                alert(response)
            }else {
                lastPref = response.lastIndexOf("]");
                gInfo += response.substring(0, lastPref + 1);
                gData = response.substring(lastPref + 2);
                //alert(gInfo);
                start(gData);
            }
        }
    })
}

function outputFullGameData(selected) {
    let gInfo = "The PGN file for this game is: \n";
    $.ajax({
        url: '/games/getOne',
        type: 'POST',
        data: {"gameId": selected},
        success: response => {
            if (response == "Error: This game doesn't exist"){
                alert(response)
            }else{
                gInfo += response;
                alert(gInfo);
            }
        }
    })
}