const express = require('express');
const router = express.Router();

const Info = require('../Modules/info.js');
router.post('/', async (req, res) => {
    try {
        //find random post from data base and serve to the client
        const randomPost = await Info.aggregate([{ $sample: { size: 1 } }]);
        res.status(200).json(randomPost[0]);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

module.exports = router;