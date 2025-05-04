const express = require('express');
const { PrismaClient } = require('@prisma/client');
const cors = require('cors');

const app = express();
const prisma = new PrismaClient();
app.use(cors());
app.use(express.json());

app.get('/enseignants', async (req, res) => {
  try {
    const enseignants = await prisma.enseignants.findMany();
    res.json(enseignants);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

app.post('/enseignants', async (req, res) => {
  const { matricule, nom, taux_horaire, nombre_heure } = req.body;
  const newEnseignant = await prisma.enseignants.create({
    data: { matricule, nom, taux_horaire, nombre_heure },
  });
  res.json(newEnseignant);
});

app.get('/enseignants/:id', async (req, res) => {
  const { id } = req.params;

  try {
    const enseignant = await prisma.enseignants.findUnique({
      where: { id: Number(id) },
    });

    if (!enseignant) {
      return res.status(404).json({ error: "Enseignant not found" });
    }

    res.json(enseignant);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: "Server error" });
  }
});

app.put('/enseignants/:id', async (req, res) => {
  const { id } = req.params;
  const { matricule, nom, taux_horaire, nombre_heure } = req.body;

  try {
    const updated = await prisma.enseignants.update({
      where: { id: Number(id) },
      data: { matricule, nom, taux_horaire, nombre_heure },
    });
    res.json(updated);
  } catch (err) {
    res.status(404).json({ error: "Enseignant not found or update failed" });
  }
});

app.delete('/enseignants/:id', async (req, res) => {
  const { id } = req.params;

  try {
    await prisma.enseignants.delete({
      where: { id: Number(id) },
    });
    res.json({ message: 'Enseignant deleted' });
  } catch (err) {
    res.status(404).json({ error: "Enseignant not found or delete failed" });
  }
});

app.get('/', (req, res) => {
  res.send('API is working');
});

app.listen(3000, () => {
  console.log("API running at http://localhost:3000");
});