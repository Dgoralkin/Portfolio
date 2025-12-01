document.addEventListener('DOMContentLoaded', function () {
  const subscribeButton = document.getElementById('subscribe-btn');
  const emailInput = document.getElementById('subscribe-email');

  if (subscribeButton && emailInput) {
    subscribeButton.addEventListener('click', function () {
      const email = emailInput.value.trim();

      // Check if empty
      if (email === '') {
        alert('Please enter your email address.');
        emailInput.focus();
        return;
      }

      // Basic email format check
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        alert('Please enter a valid email address.');
        emailInput.focus();
        return;
      }

      // Passed validation
      alert('Thank you for subscribing with: ' + email);
      emailInput.value = ''; // Clear after submission
    });
  }
});




document.addEventListener('DOMContentLoaded', function () {
  // ----- Buy Buttons -----
  const buyButtons = document.querySelectorAll('.buy-btn');
  buyButtons.forEach(button => {
    button.addEventListener('click', function () {
      const productDiv = button.closest('.promo-item');
      const title = productDiv?.querySelector('h3');
      if (title) {
        alert(`${title.textContent} has been added to your shopping cart.`);
      }
    });
  });
});


// ----- Contact Us -----
document.addEventListener('DOMContentLoaded', function () {
  const submitButton = document.getElementById('contact-us');
  const consentCheckbox = document.getElementById('consent');

  const nameInput = document.getElementById('name');
  const emailInput = document.getElementById('email');
  const phoneInput = document.getElementById('phone'); // optional
  const orderTextarea = document.getElementById('order');

  if (submitButton && consentCheckbox) {
    submitButton.addEventListener('click', function () {
      // Check for empty required fields
      if (!nameInput.value.trim() || !emailInput.value.trim() || !orderTextarea.value.trim()) {
        alert('Please fill out all required fields before submitting ❗');
        return;
      }

      // Check if consent is given
      if (!consentCheckbox.checked) {
        alert('Please check the consent box before submitting ❗');
        return;
      }

      // If all checks pass
      alert('Consent given ✅, your request submitted!');
    });
  }
});
