document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const loginRequest = {
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value
    };
    
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginRequest)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Credenciais inválidas');
        }
    })
    .then(data => {
        // Armazena o token JWT (se estiver usando)
        if (data.token) {
            localStorage.setItem('token', data.token);
        }
        window.location.href = '/dashboard';
    })
    .catch(error => {
        alert(error.message);
    });
});