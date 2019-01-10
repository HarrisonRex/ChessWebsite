function pageLoad() {
    resetLoginForm();
    resetNewUserForm();
}

//The form for logging in a existing user
function resetLoginForm() {
    const loginForm = $('#loginForm');
    loginForm.submit(event => {
        event.preventDefault();
    $.ajax({
        //Sends the request to the java on the server
        url: '/user/login',
        type: 'POST',
        data: loginForm.serialize(),
        success: response => {
            //Checks if the response is appropriate
        if (response.startsWith('Error:')) {
        alert(response);
    } else {
            //Sends the user to the index home page
        Cookies.set("sessionToken", response);
        window.location.href = "/client/index.html";
    }
}
});
});
}

//The form to create and login a new user
function resetNewUserForm() {
    const CreateUserForm = $('#CreateUserForm');
    CreateUserForm.submit(event => {
        event.preventDefault();
    $.ajax({
        url: '/user/new',
        type: 'POST',
        data: CreateUserForm.serialize(),
        success: response => {
        if (response.startsWith('Error:')) {
        alert(response);
    } else {
        Cookies.set("sessionToken", response);
        window.location.href = "/client/index.html";
    }
}
});
});
}