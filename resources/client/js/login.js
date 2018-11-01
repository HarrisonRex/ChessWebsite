function pageLoad() {
    resetLoginForm();
    resetNewUserForm();
}

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