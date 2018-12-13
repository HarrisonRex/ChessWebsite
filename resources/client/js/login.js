function pageLoad() {
    resetLoginForm();
    resetNewUserForm();
}

//The form for loging in a existing user
function resetLoginForm() {
    const loginForm = $('#loginForm');
    loginForm.submit(event => {
        event.preventDefault();
    $.ajax({
        url: '/user/login',
        type: 'POST',
        data: loginForm.serialize(),
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