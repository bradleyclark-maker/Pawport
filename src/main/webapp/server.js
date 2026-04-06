const express = require('express');
const nodemailer = require('nodemailer');
const cors = require('cors');
const path = require('path');

const app = express();

app.use(express.json());
app.use(cors());

// Serve frontend
app.use(express.static(path.join(__dirname, 'webproject')));

// Email setup
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'your_email@gmail.com',
        pass: 'your_app_password'
    }
});

app.post('/reminder', (req, res) => {
    const { title, minutes, email } = req.body;

    if (!title || !minutes || !email) {
        return res.send("❌ Missing fields!");
    }

    const delay = minutes * 60 * 1000;

    setTimeout(() => {
        transporter.sendMail({
            from: 'your_email@gmail.com',
            to: email,
            subject: '🐾 PawPort Reminder',
            text: `Reminder: ${title}`
        });
    }, delay);

    res.send("✅ Reminder scheduled!");
});

app.listen(5000, () => {
    console.log("🚀 Server running at http://localhost:3000");
});