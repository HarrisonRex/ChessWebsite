function pageLoad() {
    checkLogin();
    resetNewGameForm();
    resetNewFriendForm();
    loadPendingFriendsList();
    loadFriendsList();
    resetDeletePendingFriend();
    resetDeleteFriend();
    $("#gameMaker").hide();
    $("#newFriend").hide();
}

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
                pendingFriendsListHTML += "Pending Friend Requests:"
                for (let pendingFriend of pendingFriendsListList) {
                    pendingFriendsListHTML += renderPendingFriend(pendingFriend);
                }
            }
            $('#pendingFriendsShower').html(pendingFriendsListHTML);
            resetDeletePendingFriend();
        }
        })
}

function renderPendingFriend(pendingFriend) {
    return `<div>` +
        `<div class="messageTest" id="name${pendingFriend.friendsListId}">${pendingFriend.user1UN}` +
        `<button class="acceptPendingFriend" data-pendingFriend-friendListId="${pendingFriend.friendsListId}">` +
        `Accept` +
        `</button>` +
        `<button class="deletePendingFriend" id="deletePendingFriend" data-pendingFriend-friendListId="${pendingFriend.friendsListId}">` +
        `Reject` +
        `</button>` +
        `</div>` +
        `</div>`;
}

function resetDeletePendingFriend () {
    $("#deletePendingFriend").click(event => {
        const pendingFriendId = $(event.target).attr('data-pendingFriend-friendListId');

        $.ajax({
            url: '/friendsList/pendingDelete',
            type: 'POST',
            data: {"pendingFriendId": pendingFriendId},
            success: response => {
                if (response == 'OK'){
                    pageLoad();
                }else{
                    alert(response);
                }
            }
        })
    })
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
                for (let friend of friendsListList) {
                    friendsListHTML += renderFriend(friend);
                }
            }
            $('#friendsShower').html(friendsListHTML);
        }
    })
}

function renderFriend(friend) {
    return `<div>` +
        `<div class="messageTest" id="name${friend.friendsListId}">${friend.user1UN}` +
        `<button class="deleteFriend" data-friend-friendListId="${friend.friendsListId}">` +
        `Delete Friend` +
        `</button>` +
        `</div>` +
        `</div>`;
}

function resetDeleteFriend () {
    $('.deleteFriend').click(event => {
        const friendId = $(event.target).attr('data-friend-friendListId');

        $.ajax({
            url: '/friendsList/delete',
            type: 'POST',
            data: {"friendId": friendId},
            success: response => {
                if (response == 'OK'){
                    pageLoad();
                }else{
                    alert(response);
                }
            }
        })
    })
}
