<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PawPort | Document Vault</title>
    <style>
        /* Updated overall body to use the logo's slate teal for a cohesive background, but lighter */
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background-color: #E6EDEC; /* A very light tint of the slate teal */
            margin: 40px; 
            color: #2c3e50; 
        }

        /* 1. Remove the main container border and make shadow subtle */
        .container { 
            max-width: 900px; 
            margin: auto; 
            background: white; 
            padding: 40px; 
            border-radius: 12px; 
            /* Subtle shadow to define without a border */
            box-shadow: 0 4px 20px rgba(0,0,0,0.06); 
        }

        /* Update header alignment with your logo */
        .header-section { 
            display: flex; 
            align-items: center; 
            /* No bottom border here */
            padding-bottom: 20px; 
            margin-bottom: 30px; 
        }

        h1 { 
            margin: 0; 
            font-size: 2.2em; 
            color: #2c3e50; 
        }

        /* 2. Style using the signature SLATE TEAL from the logo */
        .primary-color { color: #4F797D; } /* Pure color from logo background */
        .primary-bg { background-color: #4F797D; color: white; }

        /* Style the Upload Box to be subtle yet defined, using slate teal tints */
        .upload-box { 
            background-color: #F0F4F4; /* A lighter tint of slate teal */
            padding: 25px; 
            border-radius: 10px; 
            margin-bottom: 35px; 
            border: 1px solid #4F797D; /* Using slate teal for a defined border */
        }

        .upload-box h3 { margin-top: 0; color: #4F797D; }

        label { 
            font-weight: 600; 
            color: #4F797D; 
            margin-bottom: 5px; 
            display: block; 
        }

        /* 3. Style the buttons and accent elements with the MUTED ORANGE from the logo */
        .btn { 
            background-color: #D28A5D; /* Pure color from logo's compass */
            color: white; 
            padding: 12px 24px; 
            border: none; 
            border-radius: 6px; 
            cursor: pointer; 
            font-weight: 600; 
            transition: background 0.2s ease; 
        }
        .btn:hover { background-color: #B2754E; /* A darker, richer orange on hover */ }

        /* Refined table with subtle grid */
        table { 
            width: 100%; 
            border-collapse: collapse; 
            margin-top: 10px; 
            box-shadow: 0 2px 5px rgba(0,0,0,0.02);
            border-radius: 8px;
            overflow: hidden;
        }
        
        th, td { 
            padding: 16px 12px; 
            text-align: left; 
            border-bottom: 1px solid #EAECEF; 
        }
        
        /* Using SLATE TEAL for the table header */
        th { 
            background-color: #4F797D; 
            color: white; 
            font-weight: 600; 
            text-transform: uppercase; 
            font-size: 0.85em; 
            letter-spacing: 0.05em; 
        }
        
        tr:last-child td { border-bottom: none; }
        
        /* Subtle tag for encrypted status, using slate teal tints */
        .status-tag { 
            padding: 5px 10px; 
            border-radius: 20px; 
            font-size: 0.85em; 
            font-weight: 500; 
            background: #D5E1E2; /* Slate teal tint */
            color: #4F797D; /* Slate teal */
        }
        
        /* Styling links to use the primary SLATE TEAL */
        a { color: #4F797D; text-decoration: none; font-weight: 500; }
        a:hover { text-decoration: underline; }

    </style>
</head>
<body>

<div class="container">
    <div class="header-section">
        <img src="Images/logoWithoutPawPort.png" alt="PawPort Logo" style="height: 60px; margin-right: 20px;">
        <h1 class="primary-color">PawPort Document Vault</h1>
    </div>
    
    <p style="color: #6c757d; max-width: 600px; margin-bottom: 30px;">Securely store and manage your pet's vital records, vaccinations, and certifications.</p>

    <div class="upload-box">
        <h3>Upload a New Document</h3>
        <form action="DocumentVault" method="post" enctype="multipart/form-data">
            <div style="margin-bottom: 20px;">
                <label>File Selection (e.g., PDF or Image):</label>
                <input type="file" name="file" required style="border: 1px solid #D1D5DB; padding: 10px; width: calc(100% - 22px); border-radius: 6px;">
            </div>
            <div style="margin-bottom: 25px;">
                <label>Document Category:</label>
                <select name="category" style="border: 1px solid #D1D5DB; padding: 10px; width: 100%; border-radius: 6px; font-size: 1em;">
                    <option value="Vaccination">Vaccination Record</option>
                    <option value="License">City License</option>
                    <option value="Medical">General Medical</option>
                    <option value="Insurance">Pet Insurance</option>
                </select>
            </div>
            <button type="submit" class="btn">Upload </button>
        </form>
    </div>

    <h3 class="primary-color" style="margin-bottom: 20px;">Stored Records</h3>
    <table>
        <thead>
            <tr>
                <th>Document Name</th>
                <th>Category</th>
                <th>Date Uploaded</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Rabies_Cert_Buddy.pdf</td>
                <td>Vaccination</td>
                <td>April 16, 2026</td>
                <td><span class="status-tag">Encrypted</span></td>
                <td><a href="#">View</a></td>
            </tr>
        </tbody>
    </table>
</div>

</body>
</html>