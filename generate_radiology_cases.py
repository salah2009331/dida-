import json
import random

# Load the existing 1000 cases
with open('app/src/main/assets/dental_cases.json', 'r', encoding='utf-8') as f:
    existing_cases = json.load(f)

print(f"Loaded {len(existing_cases)} existing cases.")

# Ensure existing cases have xray fields set logically based on their specialty and procedures
# If they don't have xray_type, we provide default accurate radiologic findings so all 2000 cases have complete data.
for c in existing_cases:
    if 'xray_type' not in c:
        # Assign best-fit default imaging modality
        spec = c.get('spec', '')
        tooth = c.get('tooth', '11')
        title = c.get('title', '')
        
        if 'ضرس عقل' in title or 'مطمور' in title or 'عظم' in title:
            c['xray_type'] = 'panoramic'
            c['xray_name'] = 'أشعة بانورامية كاملة (Panoramic OPG)'
            c['xray_findings'] = f'تظهر الصورة البانورامية وضعية السن #{tooth} التشريحية بالنسبة للقناة العصبية السنخية وجيوب الفك.'
        elif spec == 'ortho':
            c['xray_type'] = 'cephalometric'
            c['xray_name'] = 'أشعة سيفالومترية جانبي (Lateral Cephalometric)'
            c['xray_findings'] = 'تحديد العلاقات الهيكلية بين الفكين وزوايا القواطع العلوية والسفلية (SNA/SNB/ANB).'
        elif spec == 'pedo' and int(tooth) > 50:
            c['xray_type'] = 'periapical'
            c['xray_name'] = 'أشعة ذروية (Periapical Radiograph)'
            c['xray_findings'] = f'تقييم جذور السن اللبني #{tooth} وموقعه بالنسبة لبرعم السن الدائم النامي تحته.'
        elif spec == 'endo':
            c['xray_type'] = 'periapical'
            c['xray_name'] = 'أشعة ذروية (Periapical Radiograph)'
            c['xray_findings'] = f'تظهر أشعة الذروة للسن #{tooth} استمرار الرباط اللثوي، تقوس الجذور، وامتداد الآفة الذروية.'
        elif spec == 'radio':
            c['xray_type'] = 'periapical' if 'ذروة' in title or 'ملاصق' in title else 'cbct'
            c['xray_name'] = 'أشعة ذروية (Periapical Radiograph)' if c['xray_type'] == 'periapical' else 'تصوير طبقي مخروطي (CBCT 3D Scan)'
            c['xray_findings'] = f'فحص شعاعي دقيق للمنطقة المحيطة بالسن #{tooth}.'
        else:
            c['xray_type'] = 'bitewing'
            c['xray_name'] = 'أشعة إطباقية بينية (Bitewing Radiograph)'
            c['xray_findings'] = f'فحص مستوى العظم السنخي والتسوس الملاصق بين الأسنان للسن #{tooth}.'

print("Updated first 1000 cases with baseline radiology metadata.")
