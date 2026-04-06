const params = new URLSearchParams(window.location.search);
const userId = params.get('userId');
const url = userId 
    ? `/pawport/editprofile?format=json&userId=${userId}` 
    : '/pawport/editprofile?format=json';

fetch(url)
    .then(res => res.json())
    .then(data => {
        // Name
        document.getElementById('display-name').textContent = data.name || 'Unnamed Profile';

        // Status badge
        const statusEl = document.getElementById('display-status');
        if (data.status) {
            statusEl.textContent = data.status.charAt(0).toUpperCase() + data.status.slice(1);
            statusEl.classList.add(data.status === 'approved' ? 'status-approved' : 'status-pending');
        }

        // Photo
        if (data.hasPhoto) {
            const img = document.getElementById('profile-img');
            img.src = userId 
                ? `/pawport/editprofile/photo?userId=${userId}` 
                : '/pawport/editprofile/photo';
            img.style.display = 'block';
            document.getElementById('photo-placeholder').style.display = 'none';
        }

        // Description
        if (data.description) {
            document.getElementById('display-description').textContent = data.description;
            document.getElementById('description-section').style.display = 'block';
        }

        // Service type tags
        if (data.serviceType) {
            const tags = data.serviceType.split(',');
            const container = document.getElementById('display-tags');
            tags.forEach(tag => {
                const span = document.createElement('span');
                span.className = 'tag active';
                span.style.cursor = 'default';
                span.textContent = tag.trim();
                container.appendChild(span);
            });
            document.getElementById('type-section').style.display = 'block';
        }

        // Contact info
        if (data.contactInfo) {
            try {
                const contacts = JSON.parse(data.contactInfo);
                const container = document.getElementById('display-contacts');
                contacts.forEach(c => {
                    const row = document.createElement('div');
                    row.className = 'contact-row';
                    row.innerHTML = `<span class="contact-type-label">${c.type}:</span><span>${c.value}</span>`;
                    container.appendChild(row);
                });
                document.getElementById('contact-section').style.display = 'block';
            } catch(e) {}
        }

        // Hours
        if (data.hours) {
            try {
                const hours = JSON.parse(data.hours);
                const container = document.getElementById('display-hours');
                const days = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
                days.forEach(day => {
                    if (hours[day] && (hours[day].open || hours[day].close)) {
                        const row = document.createElement('div');
                        row.className = 'hours-row';
                        const open  = formatTime(hours[day].open);
                        const close = formatTime(hours[day].close);
                        row.innerHTML = `<span class="hours-day">${day}</span><span>${open} – ${close}</span>`;
                        container.appendChild(row);
                    }
                });
                if (container.children.length > 0) {
                    document.getElementById('hours-section').style.display = 'block';
                }
            } catch(e) {}
        }

        // Location
        if (data.location) {
            document.getElementById('display-location').textContent = data.location;
            document.getElementById('location-section').style.display = 'block';
        }
    })
    .catch(() => {
        document.getElementById('display-name').textContent = 'Could not load profile.';
    });

// Convert 24hr time to 12hr format
function formatTime(time) {
    if (!time) return '—';
    const [h, m] = time.split(':');
    const hour = parseInt(h);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const hour12 = hour % 12 || 12;
    return `${hour12}:${m} ${ampm}`;
}