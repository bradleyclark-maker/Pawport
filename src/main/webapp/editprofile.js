// Toggle sections on or off
document.querySelectorAll('.toggle').forEach(checkbox => {
    const field = checkbox.closest('.field');
    const content = field.querySelector('.field-content');

    checkbox.addEventListener('change', function () {
        content.style.display = this.checked ? 'block' : 'none';
    });
});

// Tag selection - tracks which tags are active
document.querySelectorAll('.tag').forEach(tag => {
    tag.addEventListener('click', function () {
        this.classList.toggle('active');
        updateServiceType();
    });
});

// Collect active tags into the serviceType hidden input
function updateServiceType() {
    const activeTags = [...document.querySelectorAll('.tag.active')]
        .map(tag => tag.textContent)
        .join(',');
    document.getElementById('serviceType-value').value = activeTags;
}

// Add new contact rows
function addContactRow(type = 'Email', value = '') {
    const list = document.getElementById('contact-list');

    const row = document.createElement('div');
    row.className = 'contact-row';
    row.innerHTML = `
        <select class="contact-type">
            <option value="Email">Email</option>
            <option value="Phone">Phone</option>
            <option value="Website">Website</option>
            <option value="Social Media">Social Media</option>
            <option value="Other">Other</option>
        </select>
        <input type="text" class="contact-value" placeholder="Enter contact" value="${value}">
        <span class="remove">✕</span>
    `;

    row.querySelector('.contact-type').value = type;
    row.querySelector('.remove').addEventListener('click', () => row.remove());

    list.appendChild(row);
}

document.getElementById('add-contact').addEventListener('click', () => addContactRow());

// Generate hours rows
const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
const hoursContent = document.getElementById('hours-content');

days.forEach(day => {
    const id = day.slice(0, 3).toLowerCase();
    const row = document.createElement('div');
    row.className = 'hours-row';
    row.innerHTML = `
        <label>${day}</label>
        <input type="time" id="${id}-open" step="300">
        to
        <input type="time" id="${id}-close" step="300">
    `;
    hoursContent.appendChild(row);
});

// Profile photo upload
const photoUpload = document.getElementById('photo-upload');
const profileImg = document.getElementById('profile-img');
const removePhotoBtn = document.getElementById('remove-photo');

// Preview photo when a file is selected
photoUpload.addEventListener('change', function () {
    const file = this.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = function (e) {
        profileImg.src = e.target.result;
        profileImg.style.display = 'block';
        removePhotoBtn.style.display = 'inline';
        document.getElementById('removePhoto-value').value = 'false';
    };
    reader.readAsDataURL(file);
});

// Remove photo
removePhotoBtn.addEventListener('click', function () {
    profileImg.src = '';
    profileImg.style.display = 'none';
    removePhotoBtn.style.display = 'none';
    photoUpload.value = '';
    document.getElementById('removePhoto-value').value = 'true';
});

// On form submit - collect dynamic fields into hidden inputs
document.querySelector('form').addEventListener('submit', function () {
    // Collect contact rows into JSON string
    const contacts = [...document.querySelectorAll('.contact-row')].map(row => ({
        type: row.querySelector('.contact-type').value,
        value: row.querySelector('.contact-value').value.trim()
    })).filter(c => c.value);
    document.getElementById('contactInfo-value').value = JSON.stringify(contacts);

    // Collect hours into JSON string
    const hours = {};
    days.forEach(day => {
        const id = day.slice(0, 3).toLowerCase();
        hours[day] = {
            open:  document.getElementById(id + '-open').value,
            close: document.getElementById(id + '-close').value
        };
    });
    document.getElementById('hours-value').value = JSON.stringify(hours);

    // Collect active service type tags
    updateServiceType();
});

// Load saved profile data into fields on page load
fetch('/pawport/editprofile?format=json')
    .then(res => res.json())
    .then(data => {
        // Text fields
        if (data.name)        document.getElementById('name-edit').value = data.name;
        if (data.description) document.getElementById('description-edit').value = data.description;
        if (data.location)    document.getElementById('location-edit').value = data.location;

        // Photo
        if (data.hasPhoto) {
            profileImg.src = '/pawport/editprofile/photo';
            profileImg.style.display = 'block';
            removePhotoBtn.style.display = 'inline';
        }

        // Restore active tags
        if (data.serviceType) {
            const activeTags = data.serviceType.split(',').map(t => t.trim());
            document.querySelectorAll('.tag').forEach(tag => {
                if (activeTags.includes(tag.textContent.trim())) {
                    tag.classList.add('active');
                }
            });
            document.getElementById('serviceType-value').value = data.serviceType;
        }

        // Restore contacts
        if (data.contactInfo) {
            try {
                JSON.parse(data.contactInfo).forEach(c => addContactRow(c.type, c.value));
            } catch(e) {}
        }

        // Restore hours
        if (data.hours) {
            try {
                const hoursData = JSON.parse(data.hours);
                days.forEach(day => {
                    const id = day.slice(0, 3).toLowerCase();
                    if (hoursData[day]) {
                        document.getElementById(id + '-open').value  = hoursData[day].open  || '';
                        document.getElementById(id + '-close').value = hoursData[day].close || '';
                    }
                });
            } catch(e) {}
        }
    })
    .catch(err => console.error('Could not load profile data:', err));